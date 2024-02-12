package me.nathanfallet.suitebde.models.clubs

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class CreateRoleInClubPayload(
    @PayloadProperty("string")
    @Schema("Name of the role", "President")
    val name: String,
    @PayloadProperty("boolean")
    @Schema("Is the role an admin role?", "true")
    val admin: Boolean,
    @PayloadProperty("boolean")
    @Schema("Is the role a default role?", "true")
    val default: Boolean = false,
)
