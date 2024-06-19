package com.suitebde.controllers.webhooks

import dev.kaccelero.annotations.APIMapping
import dev.kaccelero.annotations.Path
import dev.kaccelero.controllers.IUnitController
import io.ktor.server.application.*

interface IWebhooksController : IUnitController {

    @APIMapping
    @Path("POST", "/stripe")
    suspend fun stripe(call: ApplicationCall)

}
