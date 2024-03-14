package me.nathanfallet.suitebde.controllers.notifications

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.notifications.ISendNotificationUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class NotificationsController(
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val sendNotificationUseCase: ISendNotificationUseCase,
) : INotificationsController {

    override suspend fun form(call: ApplicationCall) {
        val association = requireAssociationForCallUseCase(call)

    }

    override suspend fun send(call: ApplicationCall, payload: CreateNotificationPayload) {
        val association = requireAssociationForCallUseCase(call)
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.NOTIFICATIONS_SEND inAssociation association.id)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "notifications_send_not_allowed"
        )
        if (!sendNotificationUseCase(association.id, payload)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
