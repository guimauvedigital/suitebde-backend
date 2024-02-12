package me.nathanfallet.suitebde.models.clubs

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class CreateClubPayload(
    @PayloadProperty("string")
    @Schema("Name of the club", "Club Informatique")
    val name: String,
    @PayloadProperty("string")
    @Schema("Description of the club", "Le club informatique est un club qui s'occupe de tout ce qui est informatique.")
    val description: String,
    @PayloadProperty("string")
    @Schema("Logo of the club", "https://example.com/logo.png")
    val logo: String?,
    @PayloadProperty("string")
    @Schema("Name of the default member role", "Member")
    val memberRoleName: String,
    @PayloadProperty("string")
    @Schema("Name of the default admin role", "Admin")
    val adminRoleName: String,
    @PayloadProperty("boolean")
    @Schema("Is the event validated?", "true")
    val validated: Boolean? = null,
)
