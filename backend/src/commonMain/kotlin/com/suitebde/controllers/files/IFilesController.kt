package com.suitebde.controllers.files

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import dev.kaccelero.annotations.AdminTemplateMapping
import dev.kaccelero.annotations.Path
import dev.kaccelero.commons.responses.RedirectResponse
import dev.kaccelero.controllers.IModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*
import me.nathanfallet.cloudflare.models.r2.Object

interface IFilesController : IModelController<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload> {

    @AdminTemplateMapping("admin/files/list.ftl")
    @Path("GET", "/files")
    suspend fun list(call: ApplicationCall): List<Object>

    @AdminTemplateMapping("admin/files/list.ftl")
    @Path("POST", "/files")
    suspend fun upload(call: ApplicationCall): RedirectResponse

}
