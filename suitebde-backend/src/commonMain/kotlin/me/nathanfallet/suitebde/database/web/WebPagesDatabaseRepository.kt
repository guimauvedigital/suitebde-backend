package me.nathanfallet.suitebde.database.web

import me.nathanfallet.ktorx.database.IDatabase
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.models.web.WebPagePayload
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class WebPagesDatabaseRepository(
    private val database: IDatabase,
) : IWebPagesRepository {

    override suspend fun list(parentId: String, context: IContext?): List<WebPage> {
        return database.dbQuery {
            WebPages
                .selectAll()
                .where { WebPages.associationId eq parentId }
                .map(WebPages::toWebPage)
        }
    }

    override suspend fun list(limit: Long, offset: Long, parentId: String, context: IContext?): List<WebPage> {
        return database.dbQuery {
            WebPages
                .selectAll()
                .where { WebPages.associationId eq parentId }
                .limit(limit.toInt(), offset)
                .map(WebPages::toWebPage)
        }
    }

    override suspend fun create(payload: WebPagePayload, parentId: String, context: IContext?): WebPage? {
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

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean {
        return database.dbQuery {
            WebPages.deleteWhere {
                WebPages.id eq id and (associationId eq parentId)
            }
        } == 1
    }

    override suspend fun get(id: String, parentId: String, context: IContext?): WebPage? {
        return database.dbQuery {
            WebPages
                .select { WebPages.id eq id and (WebPages.associationId eq parentId) }
                .map(WebPages::toWebPage)
                .singleOrNull()
        }
    }

    override suspend fun getByUrl(url: String, associationId: String): WebPage? {
        return database.dbQuery {
            WebPages
                .selectAll()
                .where { WebPages.associationId eq associationId and (WebPages.url eq url) }
                .map(WebPages::toWebPage)
                .singleOrNull()
        }
    }

    override suspend fun getHome(associationId: String): WebPage? {
        return database.dbQuery {
            WebPages
                .selectAll()
                .where { WebPages.associationId eq associationId and (WebPages.home eq true) }
                .map(WebPages::toWebPage)
                .singleOrNull()
        }
    }

    override suspend fun update(
        id: String,
        payload: WebPagePayload,
        parentId: String,
        context: IContext?,
    ): Boolean {
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
