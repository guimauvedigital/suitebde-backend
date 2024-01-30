package me.nathanfallet.suitebde.controllers.roles

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.roles.CreateUserInRole
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UserInRole

interface IUsersInRolesController : IChildModelController<UserInRole, String, CreateUserInRole, Unit, Role, String> {

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_in_roles_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Role,
        @Payload payload: CreateUserInRole,
    ): UserInRole

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(UserInRole::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_in_roles_not_allowed")
    @DocumentedError(404, "users_in_roles_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Role, @Id id: String)

    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Role, @Id id: String): UserInRole

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(call: ApplicationCall, @ParentModel parent: Role): List<UserInRole>

}
