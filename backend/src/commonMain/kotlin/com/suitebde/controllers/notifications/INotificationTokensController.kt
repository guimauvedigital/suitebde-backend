package com.suitebde.controllers.notifications

import com.suitebde.models.notifications.CreateNotificationTokenPayload
import com.suitebde.models.notifications.NotificationToken
import com.suitebde.models.users.User
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface INotificationTokensController :
    IChildModelController<NotificationToken, String, CreateNotificationTokenPayload, Unit, User, UUID> {

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
