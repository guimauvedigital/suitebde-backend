package me.nathanfallet.suitebde.controllers.users

import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.controllers.models.ModelRouter
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase

class UserRouter(
    userController: IUserController,
    getAssociationForDomainUseCase: IGetAssociationForDomainUseCase,
    getUserForCallUseCase: IGetUserForCallUseCase
) : ModelRouter<User, Unit, Unit>(
    "users",
    typeInfo<User>(),
    typeInfo<List<User>>(),
    typeInfo<Unit>(),
    typeInfo<Unit>(),
    userController,
    getAssociationForDomainUseCase,
    getUserForCallUseCase
)
