package me.nathanfallet.suitebde.controllers.notifications

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.models.responses.RedirectResponse
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.models.notifications.NotificationTopics
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.notifications.IListNotificationTopicsUseCase
import me.nathanfallet.suitebde.usecases.notifications.ISendNotificationUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class NotificationsController(
    private val getAssociationsUseCase: IGetModelSuspendUseCase<Association, String>,
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val listNotificationTopicsUseCase: IListNotificationTopicsUseCase,
    private val sendNotificationUseCase: ISendNotificationUseCase,
) : INotificationsController {

    override suspend fun form(call: ApplicationCall): Map<String, Any> {
        val association = requireAssociationForCallUseCase(call)
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.NOTIFICATIONS_SEND inAssociation association.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "notifications_send_not_allowed"
        )
        return mapOf(
            "topics" to listNotificationTopicsUseCase(association)
        )
    }

    override suspend fun send(call: ApplicationCall, associationId: String?, payload: CreateNotificationPayload) {
        val association = associationId?.let { getAssociationsUseCase(it) } ?: requireAssociationForCallUseCase(call)
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.NOTIFICATIONS_SEND inAssociation association.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "notifications_send_not_allowed"
        )
        val topics = listNotificationTopicsUseCase(association)
        if (payload.topic !in topics.topics) throw ControllerException(
            HttpStatusCode.BadRequest, "notifications_topic_invalid"
        )
        sendNotificationUseCase(payload)
        if (call.request.path().contains("/admin/")) throw RedirectResponse("/admin/notifications")
    }

    override suspend fun topics(call: ApplicationCall, associationId: String?): NotificationTopics {
        val association = associationId?.let { getAssociationsUseCase(it) } ?: requireAssociationForCallUseCase(call)
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.NOTIFICATIONS_SEND inAssociation association.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "notifications_send_not_allowed"
        )
        return listNotificationTopicsUseCase(association)
    }

}
