package com.suitebde.models.events

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @ModelProperty("id")
    @Schema("Id of the event", "123abc")
    override val id: UUID,
    @Schema("Id of the association the event is in", "123abc")
    val associationId: UUID,
    @ModelProperty("string")
    @Schema("Name of the event", "Soirée de Noël")
    val name: String,
    @Schema("Description of the event", "Venez fêter Noël avec nous !")
    val description: String,
    @ModelProperty("image")
    @Schema("Image of the event", "https://example.com/event.png")
    val image: String?,
    @ModelProperty("date")
    @Schema("Date the event starts at", "2023-12-13T09:41:00Z")
    val startsAt: Instant,
    @ModelProperty("date")
    @Schema("Date the event ends at", "2023-12-13T09:41:00Z")
    val endsAt: Instant,
    @ModelProperty("boolean")
    @Schema("Is the event validated?", "true")
    val validated: Boolean,
) : IChildModel<UUID, CreateEventPayload, UpdateEventPayload, UUID> {

    override val parentId: UUID
        get() = associationId

}
