package me.nathanfallet.suitebde.database.events

import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.models.events.TicketConfigurationInEvent
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object TicketConfigurationsInEvents : Table() {

    val id = varchar("id", 32)
    val eventId = varchar("event_id", 32).index()
    val name = text("name")
    val description = text("description")
    val price = long("price")
    val reducedPrice = long("reduced_price")
    val bail = long("bail")

    override val primaryKey = PrimaryKey(id)

    fun generateId(): String {
        val candidate = String.generateId()
        return if (selectAll().where { id eq candidate }.count() > 0) generateId() else candidate
    }

    fun toTicketConfigurationInEvent(
        row: ResultRow,
        event: Event? = null,
    ) = TicketConfigurationInEvent(
        row[id],
        row[eventId],
        row[name],
        row[description],
        row[price],
        row[reducedPrice],
        row[bail],
        event
    )

}
