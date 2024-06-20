package com.suitebde.controllers.roles

import com.suitebde.models.roles.CreateUserInRolePayload
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UserInRole
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IUsersInRolesController : IChildModelController<UserInRole, UUID, CreateUserInRolePayload, Unit, Role, UUID> {

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_in_roles_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Role,
        @Payload payload: CreateUserInRolePayload,
    ): UserInRole

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(UserInRole::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_in_roles_not_allowed")
    @DocumentedError(404, "users_in_roles_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Role, @Id id: UUID)

    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Role, @Id id: UUID): UserInRole

    @APIMapping
    @AdminTemplateMapping("admin/roles/users.ftl")
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Role,
        @QueryParameter limit: Long?,
        @QueryParameter offset: Long?,
    ): List<UserInRole>

}
