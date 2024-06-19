package com.suitebde.controllers.roles

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.CreateRolePayload
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UpdateRolePayload
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IRolesController :
    IChildModelController<Role, UUID, CreateRolePayload, UpdateRolePayload, Association, UUID> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @QueryParameter limit: Long?,
        @QueryParameter offset: Long?,
    ): List<Role>

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
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID): Role

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
        @Id id: UUID,
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
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID)

}
