package me.nathanfallet.suitebde.controllers.roles

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.roles.CreateUserInRole
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UserInRole
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.slice.IListSliceChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class UsersInRolesController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getUsersInRolesUseCase: IListChildModelSuspendUseCase<UserInRole, String>,
    private val getUsersInRolesSlicedUseCase: IListSliceChildModelSuspendUseCase<UserInRole, String>,
    private val createUserInRolesUseCase: ICreateChildModelSuspendUseCase<UserInRole, CreateUserInRole, String>,
    private val getUserInRoleUseCase: IGetChildModelSuspendUseCase<UserInRole, String, String>,
    private val deleteUserInRoleUseCase: IDeleteChildModelSuspendUseCase<UserInRole, String, String>,
) : IUsersInRolesController {

    override suspend fun create(call: ApplicationCall, parent: Role, payload: CreateUserInRole): UserInRole {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_USERS inAssociation parent.associationId)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_in_roles_not_allowed"
        )
        return createUserInRolesUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Role, id: String) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_USERS inAssociation parent.associationId)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "users_in_roles_not_allowed"
        )
        val userInRole = getUserInRoleUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_in_roles_not_found"
        )
        if (!deleteUserInRoleUseCase(userInRole.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Role, id: String): UserInRole {
        return getUserInRoleUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "users_in_roles_not_found"
        )
    }

    override suspend fun list(call: ApplicationCall, parent: Role): List<UserInRole> {
        if (call.request.path().contains("/admin/")) return getUsersInRolesUseCase(parent.id)
        return getUsersInRolesSlicedUseCase(
            call.parameters["limit"]?.toLongOrNull() ?: 25,
            call.parameters["offset"]?.toLongOrNull() ?: 0,
            parent.id
        )
    }

}
