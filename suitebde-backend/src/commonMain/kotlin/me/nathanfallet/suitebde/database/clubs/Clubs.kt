package me.nathanfallet.suitebde.database.clubs

import kotlinx.datetime.toInstant
import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.clubs.Club
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object Clubs : Table() {

    val id = varchar("id", 32)
    val associationId = varchar("association_id", 32).index()
    val name = text("name")
    val description = text("description")
    val icon = text("icon").nullable()
    val createdAt = varchar("created_at", 255)
    val validated = bool("validated")

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (selectAll().where { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toClub(
        row: ResultRow,
    ) = Club(
        row[id],
        row[associationId],
        row[name],
        row[description],
        row[icon],
        row[createdAt].toInstant(),
        row[validated]
    )

}
