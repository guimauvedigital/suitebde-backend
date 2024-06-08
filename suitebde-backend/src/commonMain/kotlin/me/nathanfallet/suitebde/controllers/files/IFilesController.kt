package me.nathanfallet.suitebde.controllers.files

import io.ktor.server.application.*
import me.nathanfallet.cloudflare.models.r2.Object
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.ktorx.models.annotations.AdminTemplateMapping
import me.nathanfallet.ktorx.models.annotations.Path
import me.nathanfallet.ktorx.models.responses.RedirectResponse
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload

interface IFilesController :
    IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload> {

    @AdminTemplateMapping("admin/files/list.ftl")
    @Path("GET", "/files")
    suspend fun list(call: ApplicationCall): List<Object>

    @AdminTemplateMapping("admin/files/list.ftl")
    @Path("POST", "/files")
    suspend fun upload(call: ApplicationCall): RedirectResponse

}
