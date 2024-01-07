package me.nathanfallet.suitebde.database.web

import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.web.WebMenu
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object WebMenus : Table() {

    val id = varchar("id", 32)
    val associationId = varchar("association_id", 32).index()
    val title = varchar("title", 255)
    val url = varchar("content", 255)
    val position = integer("position")
    val parentMenuId = varchar("parent_menu_id", 32).nullable()

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (selectAll().where { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toWebMenu(
        row: ResultRow,
    ) = WebMenu(
        row[id],
        row[associationId],
        row[title],
        row[url],
        row[position],
        row[parentMenuId]
    )

}
