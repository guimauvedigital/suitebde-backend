package me.nathanfallet.suitebde.repositories.web

import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IWebPagesRepository :
    IChildModelSuspendRepository<WebPage, String, CreateWebPagePayload, UpdateWebPagePayload, String> {

    suspend fun getByUrl(url: String, associationId: String): WebPage?
    suspend fun getHome(associationId: String): WebPage?

}
