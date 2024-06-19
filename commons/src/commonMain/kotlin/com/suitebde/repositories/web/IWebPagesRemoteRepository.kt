package com.suitebde.repositories.web

import com.suitebde.models.web.WebPage
import com.suitebde.models.web.WebPagePayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface IWebPagesRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: UUID): List<WebPage>
    suspend fun get(id: UUID, associationId: UUID): WebPage?
    suspend fun create(payload: WebPagePayload, associationId: UUID): WebPage?
    suspend fun update(id: UUID, payload: WebPagePayload, associationId: UUID): WebPage?
    suspend fun delete(id: UUID, associationId: UUID): Boolean

}
