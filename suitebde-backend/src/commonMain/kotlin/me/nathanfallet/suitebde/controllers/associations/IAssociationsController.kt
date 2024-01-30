package me.nathanfallet.suitebde.controllers.associations

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.ktorx.models.annotations.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload

interface IAssociationsController :
    IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload> {

    @APIMapping
    @AdminTemplateMapping
    @ListModelPath
    suspend fun list(call: ApplicationCall): List<Association>

    @APIMapping
    @GetModelPath
    @DocumentedError(404, "associations_not_found")
    suspend fun get(call: ApplicationCall, @Id id: String): Association

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    @DocumentedError(401, "auth_invalid_credentials")
    @DocumentedError(403, "associations_update_not_allowed")
    @DocumentedError(404, "associations_not_found")
    @DocumentedError(500, "error_internal")
    suspend fun update(call: ApplicationCall, @Id id: String, @Payload payload: UpdateAssociationPayload): Association

}
