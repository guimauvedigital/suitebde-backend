package me.nathanfallet.suitebde.models.clubs

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class UpdateClubPayload(
    @PayloadProperty("string")
    @Schema("Name of the club", "Club Informatique")
    val name: String? = null,
    @PayloadProperty("string")
    @Schema("Description of the club", "Le club informatique est un club qui s'occupe de tout ce qui est informatique.")
    val description: String? = null,
    @PayloadProperty("string")
    @Schema("Icon of the club", "https://example.com/icon.png")
    val icon: String? = null,
    @PayloadProperty("boolean")
    @Schema("Is the event validated?", "true")
    val validated: Boolean? = null,
)
