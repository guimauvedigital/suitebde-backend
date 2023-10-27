package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.roles.ICheckPermissionUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUsersInAssociationUseCase
import me.nathanfallet.suitebde.usecases.users.IUpdateUserUseCase

class UserController(
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    private val getUserForCallUseCase: IGetUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionUseCase,
    private val getUsersInAssociationUseCase: IGetUsersInAssociationUseCase,
    private val getUserUseCase: IGetUserUseCase,
    private val updateUserUseCase: IUpdateUserUseCase
) : IUserController {

    override suspend fun getAll(call: ApplicationCall): List<User> {
        val association = getAssociationForCallUseCase(call) ?: throw ControllerException(
            HttpStatusCode.NotFound, LocalizedString.ASSOCIATIONS_NOT_FOUND
        )
        getUserForCallUseCase(call)?.takeIf {
            checkPermissionUseCase(Pair(it, Permission.USERS_VIEW))
        } ?: throw ControllerException(
            HttpStatusCode.Unauthorized, LocalizedString.USERS_VIEW_NOT_ALLOWED
        )
        return getUsersInAssociationUseCase(association.id)
    }

    override suspend fun get(call: ApplicationCall): User {
        val association = getAssociationForCallUseCase(call) ?: throw ControllerException(
            HttpStatusCode.NotFound, LocalizedString.ASSOCIATIONS_NOT_FOUND
        )
        val id = call.parameters["id"] ?: throw ControllerException(
            HttpStatusCode.BadRequest, LocalizedString.ERROR_MISSING_ID
        )
        getUserForCallUseCase(call)?.takeIf {
            it.id.equals(id, true) || checkPermissionUseCase(Pair(it, Permission.USERS_VIEW))
        } ?: throw ControllerException(HttpStatusCode.Unauthorized, LocalizedString.USERS_VIEW_NOT_ALLOWED)
        return getUserUseCase(id)?.takeIf {
            it.associationId == association.id
        } ?: throw ControllerException(HttpStatusCode.NotFound, LocalizedString.USERS_NOT_FOUND)
    }

    override suspend fun create(call: ApplicationCall, payload: Unit): User {
        throw ControllerException(HttpStatusCode.MethodNotAllowed, LocalizedString.USERS_CREATE_NOT_ALLOWED)
    }

    override suspend fun update(call: ApplicationCall, payload: Unit): User {
        val association = getAssociationForCallUseCase(call) ?: throw ControllerException(
            HttpStatusCode.NotFound, LocalizedString.ASSOCIATIONS_NOT_FOUND
        )
        val id = call.parameters["id"] ?: throw ControllerException(
            HttpStatusCode.BadRequest, LocalizedString.ERROR_MISSING_ID
        )
        getUserForCallUseCase(call)?.takeIf {
            it.id.equals(id, true) || checkPermissionUseCase(Pair(it, Permission.USERS_UPDATE))
        } ?: throw ControllerException(HttpStatusCode.Unauthorized, LocalizedString.USERS_UPDATE_NOT_ALLOWED)
        val targetUser = getUserUseCase(id)?.takeIf {
            it.associationId == association.id
        } ?: throw ControllerException(HttpStatusCode.NotFound, LocalizedString.USERS_NOT_FOUND)
        updateUserUseCase(targetUser)
        // TODO: Update user from payload, checking if user is allowed to do so
        return targetUser
    }

    override suspend fun delete(call: ApplicationCall) {
        throw ControllerException(HttpStatusCode.MethodNotAllowed, LocalizedString.USERS_DELETE_NOT_ALLOWED)
    }

}