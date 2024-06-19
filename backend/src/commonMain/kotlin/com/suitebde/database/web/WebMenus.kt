package com.suitebde.database.web

import com.suitebde.models.web.WebMenu
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object WebMenus : UUIDTable() {

    val associationId = uuid("association_id").index()
    val title = varchar("title", 255)
    val url = text("content")
    val position = integer("position")
    val parentMenuId = uuid("parent_menu_id").nullable()

    fun toWebMenu(
        row: ResultRow,
    ) = WebMenu(
        UUID(row[id].value),
        UUID(row[associationId]),
        row[title],
        row[url],
        row[position],
        row[parentMenuId]?.let(::UUID)
    )

}
