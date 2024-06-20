package com.suitebde.controllers.clubs

import com.suitebde.models.associations.Association
import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.CreateClubPayload
import com.suitebde.models.clubs.UpdateClubPayload
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IChildModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IClubsController :
    IChildModelController<Club, UUID, CreateClubPayload, UpdateClubPayload, Association, UUID> {

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
    @TemplateMapping("public/clubs/suggest.ftl")
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
    suspend fun get(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID): Club

    @TemplateMapping("public/clubs/details.ftl")
    @Path("GET", "/{clubId}")
    suspend fun details(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID): Map<String, Any>

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
        @Id id: UUID,
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
    suspend fun delete(call: ApplicationCall, @ParentModel parent: Association, @Id id: UUID)

}
