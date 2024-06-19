package com.suitebde.controllers.clubs

import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.CreateUserInClubPayload
import com.suitebde.models.clubs.UserInClub
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IUsersInClubsController : IChildModelController<UserInClub, UUID, CreateUserInClubPayload, Unit, Club, UUID> {

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_in_clubs_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Club,
        @Payload payload: CreateUserInClubPayload,
    ): UserInClub

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(UserInClub::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "users_in_clubs_not_allowed")
    @DocumentedError(404, "users_in_clubs_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Club, @Id id: UUID)

    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Club, @Id id: UUID): UserInClub

    @APIMapping
    @AdminTemplateMapping("admin/clubs/users.ftl")
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Club,
        @QueryParameter limit: Long?,
        @QueryParameter offset: Long?,
    ): List<UserInClub>

}
