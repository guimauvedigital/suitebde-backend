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
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Role,
        @Payload payload: CreateUserInRole,
    ): UserInRole

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(UserInRole::class)
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Role, @Id id: String)

    @APIMapping
    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Role, @Id id: String): UserInRole

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(call: ApplicationCall, @ParentModel parent: Role): List<UserInRole>

}
