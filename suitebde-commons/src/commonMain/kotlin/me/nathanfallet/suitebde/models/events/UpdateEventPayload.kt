package me.nathanfallet.suitebde.models.events

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class UpdateEventPayload(
    @PayloadProperty("string")
    @Schema("Name of the event", "Soirée de Noël")
    val name: String? = null,
    @PayloadProperty("string")
    @Schema("Description of the event", "Venez fêter Noël avec nous !")
    val description: String? = null,
    @PayloadProperty("string")
    @Schema("Icon of the event", "https://example.com/icon.png")
    val icon: String? = null,
    @PayloadProperty("date", "6")
    @Schema("Date the event starts at", "2023-12-13T09:41:00Z")
    val startsAt: Instant? = null,
    @PayloadProperty("date", "6")
    @Schema("Date the event ends at", "2023-12-13T09:41:00Z")
    val endsAt: Instant? = null,
    @PayloadProperty("boolean")
    @Schema("Is the event validated?", "true")
    val validated: Boolean? = null,
)
