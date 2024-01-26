package me.nathanfallet.suitebde.database.web

import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.repositories.web.IWebMenusRepository
import me.nathanfallet.surexposed.database.IDatabase
import me.nathanfallet.usecases.context.IContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class WebMenusDatabaseRepository(
    private val database: IDatabase,
) : IWebMenusRepository {

    init {
        database.transaction {
            SchemaUtils.create(WebMenus)
        }
    }

    override suspend fun list(parentId: String, context: IContext?): List<WebMenu> {
        return database.suspendedTransaction {
            WebMenus
                .selectAll()
                .where { WebMenus.associationId eq parentId }
                .map(WebMenus::toWebMenu)
        }
    }

    override suspend fun list(limit: Long, offset: Long, parentId: String, context: IContext?): List<WebMenu> {
        return database.suspendedTransaction {
            WebMenus
                .selectAll()
                .where { WebMenus.associationId eq parentId }
                .limit(limit.toInt(), offset)
                .map(WebMenus::toWebMenu)
        }
    }

    override suspend fun get(id: String, parentId: String, context: IContext?): WebMenu? {
        return database.suspendedTransaction {
            WebMenus
                .selectAll()
                .where { WebMenus.id eq id and (WebMenus.associationId eq parentId) }
                .map(WebMenus::toWebMenu)
                .singleOrNull()
        }
    }

    override suspend fun create(payload: CreateWebMenuPayload, parentId: String, context: IContext?): WebMenu? {
        return database.suspendedTransaction {
            WebMenus.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[title] = payload.title
                it[url] = payload.url
                it[position] = payload.position ?: (WebMenus.selectAll().count().toInt() + 1)
            }.resultedValues?.map(WebMenus::toWebMenu)?.singleOrNull()
        }
    }

    override suspend fun update(
        id: String,
        payload: UpdateWebMenuPayload,
        parentId: String,
        context: IContext?,
    ): Boolean {
        return database.suspendedTransaction {
            WebMenus.update({ WebMenus.id eq id and (WebMenus.associationId eq parentId) }) {
                it[title] = payload.title
                it[url] = payload.url
                it[position] = payload.position
            }
        } == 1
    }

    override suspend fun delete(id: String, parentId: String, context: IContext?): Boolean {
        return database.suspendedTransaction {
            WebMenus.deleteWhere {
                WebMenus.id eq id and (associationId eq parentId)
            }
        } == 1
    }
}
