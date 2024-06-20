package com.suitebde.models.events

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    @ModelProperty("id")
    @Schema("Id of the ticket", "123abc")
    override val id: UUID,
    @Schema("Id of the ticket configuration the ticket is in", "123abc")
    val ticketConfigurationId: UUID,
    @Schema("Email of the user who bought the ticket", "nathan@suitebde.com")
    val email: String,
    @ModelProperty("string", "6")
    @Schema("First name of the user who bought the ticket", "Nathan")
    val firstName: String,
    @ModelProperty("string", "6")
    @Schema("Last name of the user who bought the ticket", "Fallet")
    val lastName: String,
    @Schema("Id of the user who bought the ticket, if logged", "123abc")
    val userId: UUID?,
    @Schema("Payment status of the ticket", "")
    val paid: String,
) : IChildModel<UUID, Unit, Unit, UUID> {

    override val parentId: UUID
        get() = ticketConfigurationId

}
