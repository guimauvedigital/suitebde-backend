package me.nathanfallet.suitebde.controllers.users

import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.controllers.models.ModelRouter
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase

class UserRouter(
    userController: IUserController,
    translateUseCase: ITranslateUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
) : ModelRouter<User, Unit, Unit>(
    "users",
    typeInfo<User>(),
    typeInfo<List<User>>(),
    typeInfo<Unit>(),
    typeInfo<Unit>(),
    userController,
    translateUseCase,
    getAdminMenuForCallUseCase
)
