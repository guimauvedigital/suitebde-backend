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
    suspend fun get(call: ApplicationCall, @Id id: String): Association

    @APIMapping
    @AdminTemplateMapping
    @UpdateModelPath
    suspend fun update(call: ApplicationCall, @Id id: String, @Payload payload: UpdateAssociationPayload): Association

}
