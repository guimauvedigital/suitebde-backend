package com.suitebde.database.web

import com.suitebde.models.web.WebPage
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object WebPages : UUIDTable() {

    val associationId = uuid("association_id").index()
    val url = varchar("url", 255)
    val title = varchar("title", 255)
    val content = text("content")
    val home = bool("home")

    fun toWebPage(
        row: ResultRow,
    ) = WebPage(
        UUID(row[id].value),
        UUID(row[associationId]),
        row[url],
        row[title],
        row[content],
        row[home]
    )

}
