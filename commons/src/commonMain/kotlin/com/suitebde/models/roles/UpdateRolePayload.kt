package com.suitebde.models.roles

import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRolePayload(
    @PayloadProperty("string")
    @Schema("Name of the role", "Respo Web")
    val name: String,
)
