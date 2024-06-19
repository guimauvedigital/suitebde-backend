package com.suitebde.models.scans

import com.suitebde.models.events.Event
import dev.kaccelero.annotations.Schema
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ScansForDay(
    @Schema("Date of the scans", "2021-09-01")
    val date: LocalDate,
    @Schema("List of scans for this day", "[{...}, ...]")
    val scans: List<Scan>,
    @Schema("List of events happening during this day", "[{...}, ...]")
    val events: List<Event>,
)
