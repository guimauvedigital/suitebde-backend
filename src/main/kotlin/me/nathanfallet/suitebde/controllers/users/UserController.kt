package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.suitebde.extensions.invoke
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.models.ModelKey
import me.nathanfallet.suitebde.models.models.ModelKeyType
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

    override val modelKeys = listOf(
        ModelKey("id", ModelKeyType.ID),
        ModelKey("firstName", ModelKeyType.STRING, col = 6),
        ModelKey("lastName", ModelKeyType.STRING, col = 6),
        ModelKey("email", ModelKeyType.STRING, editable = false),
    )

    private suspend fun requireAssociation(call: ApplicationCall): Association {
        return getAssociationForCallUseCase(call) ?: throw ControllerException(
            HttpStatusCode.NotFound, "associations_not_found"
        )
    }

    private suspend fun requireUser(call: ApplicationCall): User {
        return getUserForCallUseCase(call) ?: throw ControllerException(
            HttpStatusCode.Unauthorized, "auth_invalid_credentials"
        )
    }

    private fun requireId(call: ApplicationCall): String {
        return call.parameters["id"] ?: throw ControllerException(
            HttpStatusCode.BadRequest, "error_missing_id"
        )
    }

    override suspend fun getAll(call: ApplicationCall): List<User> {
        val association = requireAssociation(call)
        requireUser(call).takeIf {
            checkPermissionUseCase(it, association, Permission.USERS_VIEW)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_view_not_allowed"
        )
        return getUsersInAssociationUseCase(association.id)
    }

    override suspend fun get(call: ApplicationCall): User {
        val association = requireAssociation(call)
        val id = requireId(call)
        requireUser(call).takeIf {
            it.id == id || checkPermissionUseCase(it, association, Permission.USERS_VIEW)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_view_not_allowed"
        )
        return getUserUseCase(id)?.takeIf {
            it.associationId == association.id
        } ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_not_found"
        )
    }

    override suspend fun create(call: ApplicationCall, payload: Unit): User {
        throw ControllerException(HttpStatusCode.MethodNotAllowed, "users_create_not_allowed")
    }

    override suspend fun update(call: ApplicationCall, payload: Unit): User {
        val association = requireAssociation(call)
        val id = requireId(call)
        requireUser(call).takeIf {
            it.id == id || checkPermissionUseCase(it, association, Permission.USERS_UPDATE)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_update_not_allowed"
        )
        val targetUser = getUserUseCase(id)?.takeIf {
            it.associationId == association.id
        } ?: throw ControllerException(HttpStatusCode.NotFound, "users_not_found")
        updateUserUseCase(targetUser).takeIf { it } ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
        // TODO: Update user from payload, checking if user is allowed to do so
        return targetUser
    }

    override suspend fun delete(call: ApplicationCall) {
        throw ControllerException(HttpStatusCode.MethodNotAllowed, "users_delete_not_allowed")
    }

}