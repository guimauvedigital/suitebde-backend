package com.suitebde.repositories.web

import com.suitebde.models.web.WebPage
import com.suitebde.models.web.WebPagePayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IWebPagesRepository : IChildModelSuspendRepository<WebPage, UUID, WebPagePayload, WebPagePayload, UUID> {

    suspend fun getByUrl(url: String, associationId: UUID): WebPage?
    suspend fun getHome(associationId: UUID): WebPage?

}
