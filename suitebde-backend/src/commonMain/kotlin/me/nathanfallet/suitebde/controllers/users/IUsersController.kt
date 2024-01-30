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
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_view_not_allowed")
    suspend fun list(call: ApplicationCall, @ParentModel parent: Association): List<User>

    @APIMapping
    @GetModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_view_not_allowed")
    @DocumentedError(404, "users_not_found")
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): User

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_update_not_allowed")
    @DocumentedError(404, "users_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun update(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: String,
        @Payload payload: UpdateUserPayload,
    ): User

}
