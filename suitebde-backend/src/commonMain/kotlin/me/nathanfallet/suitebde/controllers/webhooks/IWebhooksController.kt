package me.nathanfallet.suitebde.controllers.webhooks

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IUnitController
import me.nathanfallet.ktorx.models.annotations.APIMapping
import me.nathanfallet.ktorx.models.annotations.Path

interface IWebhooksController : IUnitController {

    @APIMapping
    @Path("POST", "/stripe")
    suspend fun stripe(call: ApplicationCall)

}
