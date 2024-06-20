package com.suitebde.controllers.notifications

import com.suitebde.controllers.users.UsersRouter
import com.suitebde.models.notifications.CreateNotificationTokenPayload
import com.suitebde.models.notifications.NotificationToken
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.APIChildModelRouter
import io.ktor.util.reflect.*

class NotificationTokensRouter(
    controller: INotificationTokensController,
    usersRouter: UsersRouter,
) : APIChildModelRouter<NotificationToken, String, CreateNotificationTokenPayload, Unit, User, UUID>(
    typeInfo<NotificationToken>(),
    typeInfo<CreateNotificationTokenPayload>(),
    typeInfo<Unit>(),
    controller,
    INotificationTokensController::class,
    usersRouter,
    prefix = "/api/v1"
)
