package me.nathanfallet.suitebde.models.events

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class Ticket(
    @ModelProperty("id")
    @Schema("Id of the ticket", "123abc")
    override val id: String,
    @Schema("Id of the ticket configuration the ticket is in", "123abc")
    val ticketConfigurationId: String,
    @Schema("Email of the user who bought the ticket", "nathan@suitebde.com")
    val email: String,
    @ModelProperty("string", "6")
    @Schema("First name of the user who bought the ticket", "Nathan")
    val firstName: String,
    @ModelProperty("string", "6")
    @Schema("Last name of the user who bought the ticket", "Fallet")
    val lastName: String,
    @Schema("Id of the user who bought the ticket, if logged", "123abc")
    val userId: String?,
    @Schema("Payment status of the ticket", "")
    val paid: String,
) : IChildModel<String, Unit, Unit, String> {

    override val parentId = ticketConfigurationId

}
