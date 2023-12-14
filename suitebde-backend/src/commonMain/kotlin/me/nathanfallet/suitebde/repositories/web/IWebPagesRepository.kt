package me.nathanfallet.suitebde.repositories.web

import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IWebPagesRepository :
    IChildModelSuspendRepository<WebPage, String, WebPagePayload, WebPagePayload, String> {

    suspend fun getByUrl(url: String, associationId: String): WebPage?
    suspend fun getHome(associationId: String): WebPage?

}
