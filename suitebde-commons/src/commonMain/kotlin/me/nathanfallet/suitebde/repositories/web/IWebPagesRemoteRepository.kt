package me.nathanfallet.suitebde.repositories.web

import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload

interface IWebPagesRemoteRepository {

    suspend fun list(): List<WebPage>
    suspend fun get(id: String): WebPage?
    suspend fun create(payload: WebPagePayload): WebPage?
    suspend fun update(id: String, payload: WebPagePayload): WebPage?
    suspend fun delete(id: String): Boolean

}
