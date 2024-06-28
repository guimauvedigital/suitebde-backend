package com.suitebde.controllers.roles

import com.suitebde.models.roles.CreatePermissionInRolePayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.roles.PermissionInRole
import com.suitebde.models.roles.Role
import dev.kaccelero.annotations.ModelAnnotations
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.permissions.ICheckPermissionSuspendUseCase
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IDeleteChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IGetChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.commons.responses.RedirectResponse
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import kotlin.reflect.typeOf

class PermissionsInRolesController(
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val checkPermissionUseCase: ICheckPermissionSuspendUseCase,
    private val getPermissionsInRolesUseCase: IListChildModelSuspendUseCase<PermissionInRole, UUID>,
    private val createPermissionInRolesUseCase: ICreateChildModelSuspendUseCase<PermissionInRole, CreatePermissionInRolePayload, UUID>,
    private val getPermissionInRoleUseCase: IGetChildModelSuspendUseCase<PermissionInRole, Permission, UUID>,
    private val deletePermissionInRoleUseCase: IDeleteChildModelSuspendUseCase<PermissionInRole, Permission, UUID>,
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

    override suspend fun delete(call: ApplicationCall, parent: Role, id: Permission) {
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

    override suspend fun get(call: ApplicationCall, parent: Role, id: Permission): PermissionInRole {
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
