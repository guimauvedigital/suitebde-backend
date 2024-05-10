package me.nathanfallet.suitebde.controllers.roles

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.ktorx.models.responses.RedirectResponse
import me.nathanfallet.suitebde.models.roles.CreatePermissionInRolePayload
import me.nathanfallet.suitebde.models.roles.PermissionInRole
import me.nathanfallet.suitebde.models.roles.Role

interface IPermissionsInRolesController :
    IChildModelController<PermissionInRole, String, CreatePermissionInRolePayload, Unit, Role, String> {

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
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Role, @Id id: String)

    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Role, @Id id: String): PermissionInRole

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
