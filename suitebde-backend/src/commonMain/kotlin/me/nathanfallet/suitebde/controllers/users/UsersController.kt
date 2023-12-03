package me.nathanfallet.suitebde.controllers.users

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class UsersController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getUsersInAssociationUseCase: IListChildModelSuspendUseCase<User, String>,
    private val getUserUseCase: IGetChildModelSuspendUseCase<User, String, String>,
    private val updateUserUseCase: IUpdateChildModelSuspendUseCase<User, String, UpdateUserPayload, String>
) : IChildModelController<User, String, CreateUserPayload, UpdateUserPayload, Association, String> {

    override suspend fun list(call: ApplicationCall, parent: Association): List<User> {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.USERS_VIEW inAssociation parent)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_view_not_allowed"
        )
        return getUsersInAssociationUseCase(parent.id)
    }

    override suspend fun get(call: ApplicationCall, parent: Association, id: String): User {
        (requireUserForCallUseCase(call) as User).takeIf {
            it.id == id || checkPermissionUseCase(it, Permission.USERS_VIEW inAssociation parent)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_view_not_allowed"
        )
        return getUserUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_not_found"
        )
    }

    override suspend fun create(call: ApplicationCall, parent: Association, payload: CreateUserPayload): User {
        throw ControllerException(HttpStatusCode.MethodNotAllowed, "users_create_not_allowed")
    }

    override suspend fun update(
        call: ApplicationCall,
        parent: Association,
        id: String,
        payload: UpdateUserPayload
    ): User {
        (requireUserForCallUseCase(call) as User).takeIf {
            it.id == id || checkPermissionUseCase(it, Permission.USERS_UPDATE inAssociation parent)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_update_not_allowed"
        )
        val targetUser = getUserUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_not_found"
        )
        return updateUserUseCase(
            targetUser.id, payload, parent.id
        ) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Association, id: String) {
        throw ControllerException(HttpStatusCode.MethodNotAllowed, "users_delete_not_allowed")
    }

}
