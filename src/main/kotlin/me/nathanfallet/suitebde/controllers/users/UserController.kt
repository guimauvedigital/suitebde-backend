package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.ktor.util.reflect.*
import me.nathanfallet.suitebde.controllers.AbstractModelController
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUsersInAssociationUseCase

class UserController(
    private val checkPermissionUseCase: ICheckPermissionUseCase,
    private val getUsersInAssociationUseCase: IGetUsersInAssociationUseCase,
    private val getUserUseCase: IGetUserUseCase,
    getAssociationForDomainUseCase: IGetAssociationForDomainUseCase,
    getUserForCallUseCase: IGetUserForCallUseCase
) : AbstractModelController<User, Unit, Unit>(
    "users",
    typeInfo<User>(),
    typeInfo<List<User>>(),
    typeInfo<Unit>(),
    typeInfo<Unit>(),
    getAssociationForDomainUseCase,
    getUserForCallUseCase
) {

    override suspend fun getAll(association: Association, user: User?): List<User> {
        user?.takeIf {
            checkPermissionUseCase(Pair(it, Permission.USERS_VIEW))
        } ?: throw ControllerException(HttpStatusCode.Unauthorized, LocalizedString.ERROR_USERS_VIEW_NOT_ALLOWED)
        return getUsersInAssociationUseCase(association.id)
    }

    override suspend fun get(association: Association, user: User?, id: String): User {
        user?.takeIf {
            user.id.equals(id, true) || checkPermissionUseCase(Pair(it, Permission.USERS_VIEW))
        } ?: throw ControllerException(HttpStatusCode.Unauthorized, LocalizedString.ERROR_USERS_VIEW_NOT_ALLOWED)
        return getUserUseCase(id)?.takeIf {
            it.associationId == association.id
        } ?: throw ControllerException(HttpStatusCode.NotFound, LocalizedString.ERROR_USERS_NOT_FOUND)
    }

    override suspend fun create(association: Association, user: User?, obj: Unit): User {
        throw ControllerException(HttpStatusCode.MethodNotAllowed, LocalizedString.ERROR_USERS_CREATE_NOT_ALLOWED)
    }

    override suspend fun update(association: Association, user: User?, id: String, obj: Unit): User {
        TODO("Not yet implemented")
    }

    override suspend fun delete(association: Association, user: User?, id: String) {
        TODO("Not yet implemented")
    }

}