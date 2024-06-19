package com.suitebde.database.events

import com.suitebde.models.events.Event
import com.suitebde.models.events.TicketConfigurationInEvent
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object TicketConfigurationsInEvents : UUIDTable() {

    val eventId = uuid("event_id").index()
    val name = text("name")
    val description = text("description")
    val price = long("price")
    val reducedPrice = long("reduced_price")
    val bail = long("bail")

    fun toTicketConfigurationInEvent(
        row: ResultRow,
        event: Event? = null,
    ) = TicketConfigurationInEvent(
        UUID(row[id].value),
        UUID(row[eventId]),
        row[name],
        row[description],
        row[price],
        row[reducedPrice],
        row[bail],
        event
    )

}
