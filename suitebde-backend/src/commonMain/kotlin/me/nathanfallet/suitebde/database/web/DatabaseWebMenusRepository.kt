package me.nathanfallet.suitebde.database.web

import me.nathanfallet.suitebde.database.Database
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.repositories.web.IWebMenusRepository
import me.nathanfallet.usecases.users.IUser
import org.jetbrains.exposed.sql.*

class DatabaseWebMenusRepository(
    private val database: Database
) : IWebMenusRepository {

    override suspend fun list(parentId: String): List<WebMenu> {
        return database.dbQuery {
            WebMenus
                .select { WebMenus.associationId eq parentId }
                .map(WebMenus::toWebMenu)
        }
    }

    override suspend fun list(limit: Long, offset: Long, parentId: String): List<WebMenu> {
        return database.dbQuery {
            WebMenus
                .select { WebMenus.associationId eq parentId }
                .limit(limit.toInt(), offset)
                .map(WebMenus::toWebMenu)
        }
    }

    override suspend fun get(id: String, parentId: String): WebMenu? {
        return database.dbQuery {
            WebMenus
                .select { WebMenus.id eq id and (WebMenus.associationId eq parentId) }
                .map(WebMenus::toWebMenu)
                .singleOrNull()
        }
    }

    override suspend fun create(payload: CreateWebMenuPayload, parentId: String, user: IUser?): WebMenu? {
        return database.dbQuery {
            WebMenus.insert {
                it[id] = generateId()
                it[associationId] = parentId
                it[title] = payload.title
                it[url] = payload.url
                it[position] = payload.position ?: (WebMenus.selectAll().count().toInt() + 1)
            }.resultedValues?.map(WebMenus::toWebMenu)?.singleOrNull()
        }
    }

    override suspend fun update(id: String, payload: UpdateWebMenuPayload, parentId: String, user: IUser?): Boolean {
        return database.dbQuery {
            WebMenus.update({ WebMenus.id eq id and (WebMenus.associationId eq parentId) }) {
                it[title] = payload.title
                it[url] = payload.url
                it[position] = payload.position
            }
        } == 1
    }

    override suspend fun delete(id: String, parentId: String): Boolean {
        return database.dbQuery {
            WebMenus.deleteWhere {
                Op.build { WebMenus.id eq id and (associationId eq parentId) }
            }
        } == 1
    }
}
