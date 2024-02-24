package me.nathanfallet.suitebde.controllers.dashboard

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IModelController
import me.nathanfallet.ktorx.models.annotations.AdminTemplateMapping
import me.nathanfallet.ktorx.models.annotations.Path
import me.nathanfallet.ktorx.models.responses.RedirectResponse
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload

interface IDashboardController :
    IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload> {

    @AdminTemplateMapping("admin/dashboard/dashboard.ftl")
    @Path("GET", "/")
    suspend fun dashboard()

    @AdminTemplateMapping("admin/dashboard/settings.ftl")
    @Path("GET", "/settings")
    suspend fun settings(call: ApplicationCall)

    @AdminTemplateMapping("admin/dashboard/settings.ftl")
    @Path("GET", "/settings/stripe")
    suspend fun settingsStripe(call: ApplicationCall): RedirectResponse

}
