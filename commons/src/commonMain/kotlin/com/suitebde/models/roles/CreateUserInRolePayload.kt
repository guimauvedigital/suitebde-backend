package com.suitebde.models.roles

import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserInRolePayload(
    @PayloadProperty("user")
    @Schema("Id of the user to add", "abc123")
    val userId: UUID,
)
