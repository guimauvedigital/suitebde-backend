package me.nathanfallet.suitebde.controllers.web

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.IChildModelController
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload

interface IWebPagesController :
    IChildModelController<WebPage, String, WebPagePayload, WebPagePayload, Association, String> {

    suspend fun getHome(call: ApplicationCall, parent: Association): WebPage
    suspend fun getByUrl(call: ApplicationCall, parent: Association, url: String): WebPage

}
