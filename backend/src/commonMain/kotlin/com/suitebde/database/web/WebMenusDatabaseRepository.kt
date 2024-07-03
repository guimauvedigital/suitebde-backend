package com.suitebde.database.web

import com.suitebde.models.web.CreateWebMenuPayload
import com.suitebde.models.web.UpdateWebMenuPayload
import com.suitebde.models.web.WebMenu
import com.suitebde.repositories.web.IWebMenusRepository
import dev.kaccelero.database.IDatabase
import dev.kaccelero.database.eq
import dev.kaccelero.database.set
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import org.jetbrains.exposed.sql.*

class WebMenusDatabaseRepository(
    private val database: IDatabase,
) : IWebMenusRepository {

    init {
        database.transaction {
            SchemaUtils.create(WebMenus)
        }
    }

    override suspend fun list(parentId: UUID, context: IContext?): List<WebMenu> =
        database.suspendedTransaction {
            WebMenus
                .selectAll()
                .where { WebMenus.associationId eq parentId }
                .orderBy(WebMenus.position)
                .map(WebMenus::toWebMenu)
        }

    override suspend fun list(pagination: Pagination, parentId: UUID, context: IContext?): List<WebMenu> =
        database.suspendedTransaction {
            WebMenus
                .selectAll()
                .where { WebMenus.associationId eq parentId }
                .orderBy(WebMenus.position)
                .limit(pagination.limit.toInt(), pagination.offset)
                .map(WebMenus::toWebMenu)
        }

    override suspend fun get(id: UUID, parentId: UUID, context: IContext?): WebMenu? =
        database.suspendedTransaction {
            WebMenus
                .selectAll()
                .where { WebMenus.id eq id and (WebMenus.associationId eq parentId) }
                .map(WebMenus::toWebMenu)
                .singleOrNull()
        }

    override suspend fun create(payload: CreateWebMenuPayload, parentId: UUID, context: IContext?): WebMenu? =
        database.suspendedTransaction {
            WebMenus.insert {
                it[associationId] = parentId
                it[title] = payload.title
                it[url] = payload.url
                it[position] = payload.position ?: (WebMenus.selectAll().count().toInt() + 1)
            }.resultedValues?.map(WebMenus::toWebMenu)?.singleOrNull()
        }

    override suspend fun update(
        id: UUID,
        payload: UpdateWebMenuPayload,
        parentId: UUID,
        context: IContext?,
    ): Boolean =
        database.suspendedTransaction {
            WebMenus.update({ WebMenus.id eq id and (WebMenus.associationId eq parentId) }) {
                it[title] = payload.title
                it[url] = payload.url
                it[position] = payload.position
            }
        } == 1

    override suspend fun delete(id: UUID, parentId: UUID, context: IContext?): Boolean =
        database.suspendedTransaction {
            WebMenus.deleteWhere {
                WebMenus.id eq id and (associationId eq parentId)
            }
        } == 1

}
