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
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateRolePayload,
    ): Role

    @APIMapping
    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): Role

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
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
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

}
