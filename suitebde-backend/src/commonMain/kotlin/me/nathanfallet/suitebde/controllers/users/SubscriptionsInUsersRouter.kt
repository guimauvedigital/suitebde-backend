package me.nathanfallet.suitebde.controllers.users

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.suitebde.models.users.CreateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.UpdateSubscriptionInUserPayload
import me.nathanfallet.suitebde.models.users.User

class SubscriptionsInUsersRouter(
    controller: ISubscriptionsInUsersController,
    usersRouter: UsersRouter,
) : APIChildModelRouter<SubscriptionInUser, String, CreateSubscriptionInUserPayload, UpdateSubscriptionInUserPayload, User, String>(
    typeInfo<SubscriptionInUser>(),
    typeInfo<CreateSubscriptionInUserPayload>(),
    typeInfo<UpdateSubscriptionInUserPayload>(),
    controller,
    ISubscriptionsInUsersController::class,
    usersRouter,
    route = "subscriptions",
    prefix = "/api/v1"
)
