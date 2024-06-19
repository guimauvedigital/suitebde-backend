package com.suitebde.database.events

import com.suitebde.models.events.Event
import dev.kaccelero.models.UUID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Events : UUIDTable() {

    val associationId = uuid("association_id").index()
    val name = text("name")
    val description = text("description")
    val image = text("image").nullable()
    val startsAt = timestamp("starts_at")
    val endsAt = timestamp("ends_at")
    val validated = bool("validated")

    fun toEvent(
        row: ResultRow,
    ) = Event(
        UUID(row[id].value),
        UUID(row[associationId]),
        row[name],
        row[description],
        row[image],
        row[startsAt],
        row[endsAt],
        row[validated]
    )

}
