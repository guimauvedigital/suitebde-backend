package me.nathanfallet.suitebde.controllers.roles

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.roles.CreatePermissionInRole
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.roles.PermissionInRole
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase

class PermissionsInRolesController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getPermissionsInRolesUseCase: IListChildModelSuspendUseCase<PermissionInRole, String>,
    private val createPermissionInRolesUseCase: ICreateChildModelSuspendUseCase<PermissionInRole, CreatePermissionInRole, String>,
    private val getPermissionInRoleUseCase: IGetChildModelSuspendUseCase<PermissionInRole, String, String>,
    private val deletePermissionInRoleUseCase: IDeleteChildModelSuspendUseCase<PermissionInRole, String, String>,
) : IPermissionsInRolesController {

    override suspend fun create(
        call: ApplicationCall,
        parent: Role,
        payload: CreatePermissionInRole,
    ): PermissionInRole {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_USERS inAssociation parent.associationId)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "permissions_in_roles_not_allowed"
        )
        return createPermissionInRolesUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Role, id: String) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_USERS inAssociation parent.associationId)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "permissions_in_roles_not_allowed"
        )
        val permissionInRole = getPermissionInRoleUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "permissions_in_roles_not_found"
        )
        if (!deletePermissionInRoleUseCase(permissionInRole.id, parent.id)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun get(call: ApplicationCall, parent: Role, id: String): PermissionInRole {
        return getPermissionInRoleUseCase(id, parent.id) ?: throw ControllerException(
            HttpStatusCode.NotFound, "permissions_in_roles_not_found"
        )
    }

    override suspend fun list(call: ApplicationCall, parent: Role): List<PermissionInRole> {
        return getPermissionsInRolesUseCase(parent.id)
    }

}
