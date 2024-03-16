package me.nathanfallet.suitebde.controllers.notifications

import io.ktor.util.reflect.*
import me.nathanfallet.ktorx.routers.api.APIChildModelRouter
import me.nathanfallet.suitebde.controllers.users.UsersRouter
import me.nathanfallet.suitebde.models.notifications.CreateNotificationTokenPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.suitebde.models.users.User

class NotificationTokensRouter(
    controller: INotificationTokensController,
    usersRouter: UsersRouter,
) : APIChildModelRouter<NotificationToken, String, CreateNotificationTokenPayload, Unit, User, String>(
    typeInfo<NotificationToken>(),
    typeInfo<CreateNotificationTokenPayload>(),
    typeInfo<Unit>(),
    controller,
    INotificationTokensController::class,
    usersRouter,
    prefix = "/api/v1"
)
