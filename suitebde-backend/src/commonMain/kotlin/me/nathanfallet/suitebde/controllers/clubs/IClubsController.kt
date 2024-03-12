package me.nathanfallet.suitebde.controllers.clubs

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.UpdateClubPayload

interface IClubsController :
    IChildModelController<Club, String, CreateClubPayload, UpdateClubPayload, Association, String> {

    @APIMapping
    @AdminTemplateMapping
    @TemplateMapping("public/clubs/list.ftl")
    @ListModelPath
    suspend fun list(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @QueryParameter limit: Long?,
        @QueryParameter offset: Long?,
        @QueryParameter search: String?,
    ): List<Club>

    @APIMapping
    @AdminTemplateMapping
    @CreateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "clubs_validated_not_allowed")
    @DocumentedError(500, "error_internal")
    suspend fun create(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Payload payload: CreateClubPayload,
    ): Club

    @APIMapping
    @GetModelPath
    @DocumentedError(404, "clubs_not_found")
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): Club

    @TemplateMapping("public/clubs/details.ftl")
    @Path("GET", "/{clubId}")
    suspend fun details(call: ApplicationCall, @ParentModel parent: Association, @Id id: String): Map<String, Any>

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "clubs_update_not_allowed")
    @DocumentedError(404, "clubs_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun update(
        call: ApplicationCall,
        @ParentModel parent: Association,
        @Id id: String,
        @Payload payload: UpdateClubPayload,
    ): Club

    @APIMapping
    @AdminTemplateMapping
    @DeleteModelPath
    @DocumentedType(Club::class)
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "clubs_delete_not_allowed")
    @DocumentedError(404, "clubs_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: String)

}
