package com.suitebde.controllers.users

import com.suitebde.models.users.CreateSubscriptionInUserPayload
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.UpdateSubscriptionInUserPayload
import com.suitebde.models.users.User
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface ISubscriptionsInUsersController :
    IChildModelController<SubscriptionInUser, UUID, CreateSubscriptionInUserPayload, UpdateSubscriptionInUserPayload, User, UUID> {

    @APIMapping
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: User,
    ): List<SubscriptionInUser>

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "subscriptions_create_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: User,
        @Payload payload: CreateSubscriptionInUserPayload,
    ): SubscriptionInUser

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "subscriptions_update_not_allowed")
    @DocumentedError(404, "subscriptions_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun update(
        call: ApplicationCall,
        @ParentModel parent: User,
        @Id id: UUID,
        @Payload payload: UpdateSubscriptionInUserPayload,
    ): SubscriptionInUser

}
