package me.nathanfallet.suitebde.repositories.web

import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload
import me.nathanfallet.usecases.pagination.Pagination

interface IWebPagesRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: String): List<WebPage>
    suspend fun get(id: String, associationId: String): WebPage?
    suspend fun create(payload: WebPagePayload, associationId: String): WebPage?
    suspend fun update(id: String, payload: WebPagePayload, associationId: String): WebPage?
    suspend fun delete(id: String, associationId: String): Boolean

}
