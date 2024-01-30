package me.nathanfallet.suitebde.controllers.roles

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.roles.CreateRolePayload
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UpdateRolePayload

interface IRolesController :
    IChildModelController<Role, String, CreateRolePayload, UpdateRolePayload, Association, String> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(call: ApplicationCall, @ParentModel parent: Association): List<Role>

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "roles_validated_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateRolePayload,
    ): Role

    @APIMapping
    @GetModelPath
    @DocumentedError(404, "roles_not_found")
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): Role

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "roles_update_not_allowed")
    @DocumentedError(404, "roles_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun update(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: String,
        @Payload payload: UpdateRolePayload,
    ): Role

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(Role::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "roles_delete_not_allowed")
    @DocumentedError(404, "roles_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

}
