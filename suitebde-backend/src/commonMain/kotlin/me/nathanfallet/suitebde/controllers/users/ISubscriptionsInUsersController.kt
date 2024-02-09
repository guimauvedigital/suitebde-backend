package me.nathanfallet.suitebde.controllers.users

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.APIMapping
import me.nathanfallet.ktorx.models.annotations.ListModelPath
import me.nathanfallet.ktorx.models.annotations.ParentModel
import me.nathanfallet.suitebde.models.users.CreateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.UpdateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.User

interface ISubscriptionsInUsersController :
    IChildModelController<SubscriptionInUser, String, CreateSubscriptionInUserPayload, UpdateSubscriptionInUserPayload, User, String> {

    @APIMapping
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: User,
    ): List<SubscriptionInUser>

}
