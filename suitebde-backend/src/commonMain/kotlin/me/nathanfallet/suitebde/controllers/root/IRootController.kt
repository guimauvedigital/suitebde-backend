package me.nathanfallet.suitebde.controllers.root

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IUnitController
import me.nathanfallet.ktorx.models.annotations.Path
import me.nathanfallet.ktorx.models.annotations.TemplateMapping
import me.nathanfallet.ktorx.models.responses.RedirectResponse

interface IRootController : IUnitController {

    @TemplateMapping("root/home.ftl")
    @Path("GET", "/")
    suspend fun redirect(call: ApplicationCall): RedirectResponse

    @TemplateMapping("root/home.ftl")
    @Path("GET", "/home")
    fun home()

}
