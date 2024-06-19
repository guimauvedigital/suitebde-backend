package com.suitebde.controllers.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import dev.kaccelero.annotations.*
import dev.kaccelero.controllers.IModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IAssociationsController :
    IModelController<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(call: ApplicationCall): List<Association>

    @APIMapping
    @GetModelPath
    @DocumentedError(404, "associations_not_found")
    suspend fun get(call: ApplicationCall, @Id id: UUID): Association

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "associations_update_not_allowed")
    @DocumentedError(404, "associations_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun update(call: ApplicationCall, @Id id: UUID, @Payload payload: UpdateAssociationPayload): Association

}
