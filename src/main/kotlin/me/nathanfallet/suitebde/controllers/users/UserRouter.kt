package me.nathanfallet.suitebde.controllers.users

import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.controllers.models.ModelRouter
import me.nathanfallet.suitebde.models.users.User

class UserRouter(
    userController: IUserController
) : ModelRouter<User, Unit, Unit>(
    "users",
    typeInfo<User>(),
    typeInfo<List<User>>(),
    typeInfo<Unit>(),
    typeInfo<Unit>(),
    userController
)
