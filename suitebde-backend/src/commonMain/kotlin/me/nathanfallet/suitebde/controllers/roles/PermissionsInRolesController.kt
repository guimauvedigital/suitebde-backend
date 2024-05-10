package me.nathanfallet.suitebde.controllers.roles

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.models.responses.RedirectResponse
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.roles.CreatePermissionInRolePayload
import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.roles.PermissionInRole
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.usecases.models.annotations.ModelAnnotations
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import me.nathanfallet.usecases.permissions.ICheckPermissionSuspendUseCase
import kotlin.reflect.typeOf

class PermissionsInRolesController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getPermissionsInRolesUseCase: IListChildModelSuspendUseCase<PermissionInRole, String>,
    private val createPermissionInRolesUseCase: ICreateChildModelSuspendUseCase<PermissionInRole, CreatePermissionInRolePayload, String>,
    private val getPermissionInRoleUseCase: IGetChildModelSuspendUseCase<PermissionInRole, String, String>,
    private val deletePermissionInRoleUseCase: IDeleteChildModelSuspendUseCase<PermissionInRole, String, String>,
) : IPermissionsInRolesController {

    override suspend fun create(
        call: ApplicationCall,
        parent: Role,
        payload: CreatePermissionInRolePayload,
    ): PermissionInRole {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_PERMISSIONS inAssociation parent.associationId)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "permissions_in_roles_not_allowed"
        )
        return createPermissionInRolesUseCase(payload, parent.id) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
    }

    override suspend fun delete(call: ApplicationCall, parent: Role, id: String) {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_PERMISSIONS inAssociation parent.associationId)
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

    override suspend fun listAdmin(call: ApplicationCall, parent: Role): Map<String, Any> {
        return mapOf(
            "items" to getPermissionsInRolesUseCase(parent.id).map { it.permission },
            "permissions" to Permission.entries
        )
    }

    override suspend fun updateAdmin(
        call: ApplicationCall,
        parent: Role,
    ): RedirectResponse {
        requireUserForCallUseCase(call).takeIf {
            checkPermissionUseCase(it, Permission.ROLES_PERMISSIONS inAssociation parent.associationId)
        } ?: throw ControllerException(
            HttpStatusCode.Forbidden, "permissions_in_roles_not_allowed"
        )
        val permissions = getPermissionsInRolesUseCase(parent.id)
        val payload = call.receiveParameters().toMap().mapValues {
            ModelAnnotations.constructPrimitiveFromString<Boolean>(typeOf<Boolean>(), it.value.first())
        }
        Permission.entries.forEach {
            if (payload[it.name] == true && permissions.none { p -> p.permission == it }) {
                createPermissionInRolesUseCase(CreatePermissionInRolePayload(it), parent.id)
            } else if (payload[it.name] != true && permissions.any { p -> p.permission == it }) {
                deletePermissionInRoleUseCase(permissions.first { p -> p.permission == it }.id, parent.id)
            }
        }
        return RedirectResponse("update")
    }

}
