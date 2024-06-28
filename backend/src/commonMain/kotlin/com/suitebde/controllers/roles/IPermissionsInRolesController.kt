package com.suitebde.controllers.roles

import com.suitebde.models.roles.CreatePermissionInRolePayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.roles.PermissionInRole
import com.suitebde.models.roles.Role
import dev.kaccelero.annotations.*
import dev.kaccelero.commons.responses.RedirectResponse
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IPermissionsInRolesController :
    IChildModelController<PermissionInRole, Permission, CreatePermissionInRolePayload, Unit, Role, UUID> {

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "permissions_in_roles_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Role,
        @Payload payload: CreatePermissionInRolePayload,
    ): PermissionInRole

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(PermissionInRole::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "permissions_in_roles_not_allowed")
    @DocumentedError(404, "permissions_in_roles_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Role, @Id id: Permission)

    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Role, @Id id: Permission): PermissionInRole

    @APIMapping
    @ListModelPath
    suspend fun list(call: ApplicationCall, @ParentModel parent: Role): List<PermissionInRole>

    @AdminTemplateMapping("admin/roles/permissions.ftl")
    @ListModelPath
    suspend fun listAdmin(call: ApplicationCall, @ParentModel parent: Role): Map<String, Any>

    @AdminTemplateMapping("admin/roles/permissions.ftl")
    @Path("POST", "/")
    suspend fun updateAdmin(
        call: ApplicationCall,
        @ParentModel parent: Role,
    ): RedirectResponse

}
