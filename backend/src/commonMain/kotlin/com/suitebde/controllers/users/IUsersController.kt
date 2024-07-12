package com.suitebde.controllers.users

import com.suitebde.models.associations.Association
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import dev.kaccelero.annotations.*
import dev.kaccelero.commons.responses.BytesResponse
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IUsersController :
    IChildModelController<User, UUID, CreateUserPayload, UpdateUserPayload, Association, UUID> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_view_not_allowed")
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @QueryParameter limit: Long?,
        @QueryParameter offset: Long?,
        @QueryParameter search: String?,
    ): List<User>

    @APIMapping
    @TemplateMapping("public/users/details.ftl")
    @GetModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_view_not_allowed")
    @DocumentedError(404, "users_not_found")
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID): User

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
        @Id id: UUID,
        @Payload payload: UpdateUserPayload,
    ): User

    @APIMapping
    @TemplateMapping("public/users/delete.ftl")
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(User::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_delete_not_allowed")
    @DocumentedError(404, "users_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID)

    @APIMapping("listUserPermissions", "Get user permissions by id")
    @Path("GET", "/{userId}/permissions")
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_view_not_allowed")
    @DocumentedError(404, "users_not_found")
    suspend fun listPermissions(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: UUID,
    ): List<Permission>

    @AdminTemplateMapping("users.csv")
    @Path("GET", "/users.csv")
    suspend fun csv(call: ApplicationCall, @ParentModel parent: Association): BytesResponse

}
