package me.nathanfallet.suitebde.models.roles

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class UpdateRolePayload(
    @PayloadProperty("string")
    @Schema("Name of the role", "Respo Web")
    val name: String,
)
