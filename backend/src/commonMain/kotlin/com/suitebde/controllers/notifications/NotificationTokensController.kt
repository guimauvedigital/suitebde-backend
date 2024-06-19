package com.suitebde.controllers.notifications

import com.suitebde.models.notifications.CreateNotificationTokenPayload
import com.suitebde.models.notifications.NotificationToken
import com.suitebde.models.users.User
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*

class NotificationTokensController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val createNotificationTokenUseCase: ICreateChildModelSuspendUseCase<NotificationToken, CreateNotificationTokenPayload, UUID>,
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
