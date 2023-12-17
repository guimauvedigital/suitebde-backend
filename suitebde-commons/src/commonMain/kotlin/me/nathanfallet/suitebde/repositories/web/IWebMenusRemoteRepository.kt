package me.nathanfallet.suitebde.repositories.web

import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu

interface IWebMenusRemoteRepository {

    suspend fun list(): List<WebMenu>
    suspend fun get(id: String): WebMenu?
    suspend fun create(payload: CreateWebMenuPayload): WebMenu?
    suspend fun update(id: String, payload: UpdateWebMenuPayload): WebMenu?
    suspend fun delete(id: String): Boolean

}
