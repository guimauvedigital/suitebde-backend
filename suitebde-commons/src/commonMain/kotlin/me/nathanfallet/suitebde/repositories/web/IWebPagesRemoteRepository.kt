package me.nathanfallet.suitebde.repositories.web

import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload

interface IWebPagesRemoteRepository {

    suspend fun list(limit: Long, offset: Long, associationId: String): List<WebPage>
    suspend fun get(id: String, associationId: String): WebPage?
    suspend fun create(payload: WebPagePayload, associationId: String): WebPage?
    suspend fun update(id: String, payload: WebPagePayload, associationId: String): WebPage?
    suspend fun delete(id: String, associationId: String): Boolean

}
