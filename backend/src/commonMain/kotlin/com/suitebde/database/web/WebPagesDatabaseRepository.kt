package com.suitebde.database.web

import com.suitebde.models.web.WebPage
import com.suitebde.models.web.WebPagePayload
import com.suitebde.repositories.web.IWebPagesRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import org.jetbrains.exposed.sql.*

class WebPagesDatabaseRepository(
    private val database: IDatabase,
) : IWebPagesRepository {

    init {
        database.transaction {
            SchemaUtils.create(WebPages)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<WebPage> =
        database.suspendedTransaction {
            WebPages
                .selectAll()
                .where { WebPages.associationId eq parentId }
                .map(WebPages::toWebPage)
        }

    override suspend fun list(pagination: Pagination, parentId: UUID, context: IContext?): List<WebPage> =
        database.suspendedTransaction {
            WebPages
                .selectAll()
                .where { WebPages.associationId eq parentId }
                .limit(pagination.limit.toInt(), pagination.offset)
                .map(WebPages::toWebPage)
        }

    override suspend fun create(payload: WebPagePayload, parentId: UUID, context: IContext?): WebPage? =
        database.suspendedTransaction {
            WebPages.insert {
                it[associationId] = parentId
                it[url] = payload.url
                it[title] = payload.title
                it[content] = payload.content
                it[home] = payload.home
            }.resultedValues?.map(WebPages::toWebPage)?.singleOrNull()
        }

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            WebPages.deleteWhere {
                WebPages.id eq id and (associationId eq parentId)
            }
        } == 1

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): WebPage? =
        database.suspendedTransaction {
            WebPages
                .selectAll()
                .where { WebPages.id eq id and (WebPages.associationId eq parentId) }
                .map(WebPages::toWebPage)
                .singleOrNull()
        }

    override suspend fun getByUrl(url: String, associationId: UUID): WebPage? =
        database.suspendedTransaction {
            WebPages
                .selectAll()
                .where { WebPages.associationId eq associationId and (WebPages.url eq url) }
                .map(WebPages::toWebPage)
                .singleOrNull()
        }

    override suspend fun getHome(associationId: UUID): WebPage? =
        database.suspendedTransaction {
            WebPages
                .selectAll()
                .where { WebPages.associationId eq associationId and (WebPages.home eq true) }
                .map(WebPages::toWebPage)
                .singleOrNull()
        }

    override suspend fun update(
        id: UUID,
        payload: WebPagePayload,
        parentId: UUID,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            WebPages.update({ WebPages.id eq id and (WebPages.associationId eq parentId) }) {
                it[url] = payload.url
                it[title] = payload.title
                it[content] = payload.content
                it[home] = payload.home
            }
        } == 1

}
