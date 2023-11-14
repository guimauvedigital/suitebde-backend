package me.nathanfallet.suitebde.database.web

import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class DatabaseWebPagesRepository(
    private val database: Database
) : IWebPagesRepository {

    override suspend fun create(payload: CreateWebPagePayload, parentId: String): WebPage? {
        return database.dbQuery {
            WebPages.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[title] = payload.title
                it[content] = payload.content
                it[isHome] = payload.isHome
            }.resultedValues?.map(WebPages::toWebPage)?.singleOrNull()
        }
    }

    override suspend fun delete(id: String, parentId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: String, parentId: String): WebPage? {
        return database.dbQuery {
            WebPages
                .select { WebPages.id eq id }
                .map(WebPages::toWebPage)
                .singleOrNull()
        }
    }

    override suspend fun update(id: String, payload: UpdateWebPagePayload, parentId: String): Boolean {
        TODO("Not yet implemented")
    }

}
