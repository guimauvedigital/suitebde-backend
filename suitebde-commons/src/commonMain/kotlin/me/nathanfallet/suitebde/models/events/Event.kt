package me.nathanfallet.suitebde.models.events

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class Event(
    @ModelProperty("id")
    @Schema("Id of the event", "123abc")
    override val id: String,
    @Schema("Id of the association the event is in", "123abc")
    val associationId: String,
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
) : IChildModel<String, CreateEventPayload, UpdateEventPayload, String> {

    override val parentId: String
        get() = associationId

}
