package me.nathanfallet.suitebde.database.web

import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository
import org.jetbrains.exposed.sql.*

class DatabaseWebPagesRepository(
    private val database: Database
) : IWebPagesRepository {

    override suspend fun getWebPages(associationId: String): List<WebPage> {
        return database.dbQuery {
            WebPages
                .select { WebPages.associationId eq associationId }
                .map(WebPages::toWebPage)
        }
    }

    override suspend fun create(payload: CreateWebPagePayload, parentId: String): WebPage? {
        return database.dbQuery {
            WebPages.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[url] = payload.url
                it[title] = payload.title
                it[content] = payload.content
                it[home] = payload.home
            }.resultedValues?.map(WebPages::toWebPage)?.singleOrNull()
        }
    }

    override suspend fun delete(id: String, parentId: String): Boolean {
        return database.dbQuery {
            WebPages.deleteWhere {
                Op.build { WebPages.id eq id and (associationId eq parentId) }
            }
        } == 1
    }

    override suspend fun get(id: String, parentId: String): WebPage? {
        return database.dbQuery {
            WebPages
                .select { WebPages.id eq id and (WebPages.associationId eq parentId) }
                .map(WebPages::toWebPage)
                .singleOrNull()
        }
    }

    override suspend fun update(id: String, payload: UpdateWebPagePayload, parentId: String): Boolean {
        return database.dbQuery {
            WebPages.update({ WebPages.id eq id and (WebPages.associationId eq parentId) }) {
                it[url] = payload.url
                it[title] = payload.title
                it[content] = payload.content
                it[home] = payload.home
            }
        } == 1
    }

}
