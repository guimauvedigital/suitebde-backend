package me.nathanfallet.suitebde.models.events

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class CreateEventPayload(
    @PayloadProperty("string")
    @Schema("Name of the event", "Soirée de Noël")
    val name: String,
    @PayloadProperty("string")
    @Schema("Description of the event", "Venez fêter Noël avec nous !")
    val description: String,
    @PayloadProperty("string")
    @Schema("Image of the event", "https://example.com/event.png")
    val image: String?,
    @PayloadProperty("date", "6")
    @Schema("Date the event starts at", "2023-12-13T09:41:00Z")
    val startsAt: Instant,
    @PayloadProperty("date", "6")
    @Schema("Date the event ends at", "2023-12-13T09:41:00Z")
    val endsAt: Instant,
    @PayloadProperty("boolean")
    @Schema("Is the event validated?", "true")
    val validated: Boolean? = null,
)
