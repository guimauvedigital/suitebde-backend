package me.nathanfallet.suitebde.database.web

import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.web.WebPage
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object WebPages : Table() {

    val id = varchar("id", 32)
    val associationId = varchar("association_id", 32).index()
    val url = varchar("url", 255)
    val title = varchar("title", 255)
    val content = text("content")
    val home = bool("home")

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (selectAll().where { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toWebPage(
        row: ResultRow,
    ) = WebPage(
        row[id],
        row[associationId],
        row[url],
        row[title],
        row[content],
        row[home]
    )

}
