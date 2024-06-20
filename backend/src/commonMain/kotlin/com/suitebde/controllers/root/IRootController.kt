package com.suitebde.controllers.root

import dev.kaccelero.annotations.Path
import dev.kaccelero.annotations.TemplateMapping
import dev.kaccelero.commons.responses.RedirectResponse
import dev.kaccelero.controllers.IUnitController
import io.ktor.server.application.*

interface IRootController : IUnitController {

    @TemplateMapping("root/home.ftl")
    @Path("GET", "/")
    suspend fun redirect(call: ApplicationCall): RedirectResponse

    @TemplateMapping("root/home.ftl")
    @Path("GET", "/home")
    fun home()

}
