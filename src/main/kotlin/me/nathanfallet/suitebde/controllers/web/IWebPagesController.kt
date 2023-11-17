package me.nathanfallet.suitebde.controllers.web

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.base.IChildModelController
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage

interface IWebPagesController :
    IChildModelController<WebPage, String, CreateWebPagePayload, UpdateWebPagePayload, Association, String> {

    suspend fun getHome(call: ApplicationCall, parent: Association): WebPage
    suspend fun getByUrl(call: ApplicationCall, parent: Association, url: String): WebPage

}
