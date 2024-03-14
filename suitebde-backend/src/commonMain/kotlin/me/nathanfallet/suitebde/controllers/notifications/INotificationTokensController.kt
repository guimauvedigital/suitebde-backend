package me.nathanfallet.suitebde.controllers.notifications

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.notifications.CreateNotificationTokenPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.suitebde.models.users.User

interface INotificationTokensController :
    IChildModelController<NotificationToken, String, CreateNotificationTokenPayload, Unit, User, String> {

    @APIMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_token_not_allowed")
    @DocumentedError(404, "users_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: User,
        @Payload payload: CreateNotificationTokenPayload,
    ): NotificationToken

}
