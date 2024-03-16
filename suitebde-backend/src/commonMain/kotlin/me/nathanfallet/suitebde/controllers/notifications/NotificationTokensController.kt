package me.nathanfallet.suitebde.controllers.notifications

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.notifications.CreateNotificationTokenPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase

class NotificationTokensController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val createNotificationTokenUseCase: ICreateChildModelSuspendUseCase<NotificationToken, CreateNotificationTokenPayload, String>,
) : INotificationTokensController {

    override suspend fun create(
        call: ApplicationCall,
        parent: User,
        payload: CreateNotificationTokenPayload,
    ): NotificationToken {
        requireUserForCallUseCase(call).takeIf { (it as User).id == parent.id } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_token_not_allowed"
        )
        return createNotificationTokenUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

}
