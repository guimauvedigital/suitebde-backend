package com.suitebde.models.events

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class TicketConfigurationInEvent(
    @ModelProperty("id")
    @Schema("Id of the ticket configuration", "123abc")
    override val id: UUID,
    @Schema("Id of the event the ticket configuration is in", "123abc")
    val eventId: UUID,
    @ModelProperty("string")
    @Schema("Name of the ticket configuration", "Ticket repas de Noël")
    val name: String,
    @ModelProperty("string")
    @Schema("Description of the ticket configuration", "Venez fêter Noël avec nous !")
    val description: String,
    @ModelProperty("long")
    @Schema("Price of the ticket", "5.0")
    val price: Long,
    @ModelProperty("long")
    @Schema("Reduced price of the ticket (for association members)", "3.0")
    val reducedPrice: Long,
    @ModelProperty("long")
    @Schema("Bail required for the event", "0.0")
    val bail: Long,
    @Schema("Event the ticket configuration is in")
    val event: Event?,
) : IChildModel<UUID, Unit, Unit, UUID> {

    override val parentId: UUID
        get() = eventId

}
