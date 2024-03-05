package me.nathanfallet.suitebde.repositories.web

import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.usecases.pagination.Pagination

interface IWebMenusRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: String): List<WebMenu>
    suspend fun get(id: String, associationId: String): WebMenu?
    suspend fun create(payload: CreateWebMenuPayload, associationId: String): WebMenu?
    suspend fun update(id: String, payload: UpdateWebMenuPayload, associationId: String): WebMenu?
    suspend fun delete(id: String, associationId: String): Boolean

}
