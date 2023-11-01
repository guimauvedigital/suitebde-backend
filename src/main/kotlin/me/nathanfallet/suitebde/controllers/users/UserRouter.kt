package me.nathanfallet.suitebde.controllers.users

import me.nathanfallet.suitebde.controllers.models.ModelRouter
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

class UserRouter(
    userController: IUserController,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
) : ModelRouter<User, Unit, UpdateUserPayload>(
    "users",
    User::class,
    Unit::class,
    UpdateUserPayload::class,
    userController,
    translateUseCase,
    getAdminMenuForCallUseCase
)
