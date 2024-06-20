package com.suitebde.controllers.dashboard

import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.associations.UpdateAssociationPayload
import dev.kaccelero.annotations.AdminTemplateMapping
import dev.kaccelero.annotations.Path
import dev.kaccelero.commons.responses.RedirectResponse
import dev.kaccelero.controllers.IModelController
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

interface IDashboardController :
    IModelController<Association, UUID, CreateAssociationPayload, UpdateAssociationPayload> {

    @AdminTemplateMapping("admin/dashboard/dashboard.ftl")
    @Path("GET", "/")
    suspend fun dashboard(call: ApplicationCall): Map<String, Any>

    @AdminTemplateMapping("admin/dashboard/settings.ftl")
    @Path("GET", "/settings")
    suspend fun settings(call: ApplicationCall): Map<String, Any>

    @AdminTemplateMapping("admin/dashboard/settings.ftl")
    @Path("GET", "/settings/stripe")
    suspend fun settingsStripe(call: ApplicationCall): RedirectResponse

}
