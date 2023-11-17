package me.nathanfallet.suitebde.database.web

import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.repositories.web.IWebMenusRepository

class DatabaseWebMenusRepository(
    private val database: Database
) : IWebMenusRepository {

    override suspend fun getMenus(associationId: String): List<WebMenu> {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String, parentId: String): WebMenu? {
        TODO("Not yet implemented")
    }

    override suspend fun create(payload: CreateWebMenuPayload, parentId: String): WebMenu? {
        TODO("Not yet implemented")
    }

    override suspend fun update(id: String, payload: UpdateWebMenuPayload, parentId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: String, parentId: String): Boolean {
        TODO("Not yet implemented")
    }
}
