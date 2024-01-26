package me.nathanfallet.suitebde.controllers.users

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User

interface IUsersController :
    IChildModelController<User, String, CreateUserPayload, UpdateUserPayload, Association, String> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(call: ApplicationCall, @ParentModel parent: Association): List<User>

    @APIMapping
    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): User

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    suspend fun update(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: String,
        @Payload payload: UpdateUserPayload,
    ): User

}
