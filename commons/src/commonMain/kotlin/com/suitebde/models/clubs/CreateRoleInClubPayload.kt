package com.suitebde.models.clubs

import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import kotlinx.serialization.Serializable

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
