package com.suitebde.controllers.notifications

import com.suitebde.models.associations.Association
import com.suitebde.models.notifications.CreateNotificationPayload
import com.suitebde.models.notifications.NotificationTopics
import com.suitebde.models.roles.Permission
import com.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import com.suitebde.usecases.notifications.IListNotificationTopicsUseCase
import com.suitebde.usecases.notifications.ISendNotificationUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.commons.responses.RedirectResponse
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*

class NotificationsController(
    private val getAssociationsUseCase: IGetModelSuspendUseCase<Association, UUID>,
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

    override suspend fun send(call: ApplicationCall, associationId: UUID?, payload: CreateNotificationPayload) {
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

    override suspend fun topics(call: ApplicationCall, associationId: UUID?): NotificationTopics {
        val association = associationId?.let { getAssociationsUseCase(it) } ?: requireAssociationForCallUseCase(call)
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.NOTIFICATIONS_SEND inAssociation association.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "notifications_send_not_allowed"
        )
        return listNotificationTopicsUseCase(association)
    }

}
