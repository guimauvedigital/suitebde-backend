package com.suitebde.controllers.users

import com.suitebde.models.users.CreateSubscriptionInUserPayload
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.UpdateSubscriptionInUserPayload
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import io.ktor.util.reflect.*

class SubscriptionsInUsersRouter(
    controller: ISubscriptionsInUsersController,
    usersRouter: UsersRouter,
) : APIChildModelRouter<SubscriptionInUser, UUID, CreateSubscriptionInUserPayload, UpdateSubscriptionInUserPayload, User, UUID>(
    typeInfo<SubscriptionInUser>(),
    typeInfo<CreateSubscriptionInUserPayload>(),
    typeInfo<UpdateSubscriptionInUserPayload>(),
    controller,
    ISubscriptionsInUsersController::class,
    usersRouter,
    route = "subscriptions",
    prefix = "/api/v1"
)
