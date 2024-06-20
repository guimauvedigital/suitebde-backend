package com.suitebde.repositories.web

import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.UpdateWebMenuPayload
import com.suitebde.models.web.WebMenu
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface IWebMenusRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: UUID): List<WebMenu>
    suspend fun get(id: UUID, associationId: UUID): WebMenu?
    suspend fun create(payload: CreateWebMenuPayload, associationId: UUID): WebMenu?
    suspend fun update(id: UUID, payload: UpdateWebMenuPayload, associationId: UUID): WebMenu?
    suspend fun delete(id: UUID, associationId: UUID): Boolean

}
