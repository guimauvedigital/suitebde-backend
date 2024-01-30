package me.nathanfallet.suitebde.models.events

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class TicketConfigurationInEvent(
    @ModelProperty("id")
    @Schema("Id of the ticket configuration", "123abc")
    override val id: String,
    @Schema("Id of the event the ticket configuration is in", "123abc")
    val eventId: String,
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
) : IChildModel<String, Unit, Unit, String> {

    override val parentId: String
        get() = eventId

}
