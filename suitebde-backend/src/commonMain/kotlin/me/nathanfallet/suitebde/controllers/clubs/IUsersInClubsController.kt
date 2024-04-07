package me.nathanfallet.suitebde.controllers.clubs

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.UserInClub

interface IUsersInClubsController :
    IChildModelController<UserInClub, String, CreateUserInClubPayload, Unit, Club, String> {

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
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Club, @Id id: String)

    @GetModelPath
    suspend fun get(call: ApplicationCall, @ParentModel parent: Club, @Id id: String): UserInClub

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
