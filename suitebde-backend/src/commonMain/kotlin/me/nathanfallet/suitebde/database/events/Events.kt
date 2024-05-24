package me.nathanfallet.suitebde.database.events

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.events.Event
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object Events : Table() {

    val id = varchar("id", 32)
    val associationId = varchar("association_id", 32).index()
    val name = text("name")
    val description = text("description")
    val image = text("image").nullable()
    val startsAt = varchar("starts_at", 255)
    val endsAt = varchar("ends_at", 255)
    val validated = bool("validated")

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (selectAll().where { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toEvent(
        row: ResultRow,
    ) = Event(
        row[id],
        row[associationId],
        row[name],
        row[description],
        row[image],
        row[startsAt].let(Instant::parse),
        row[endsAt].let(Instant::parse),
        row[validated]
    )

}
