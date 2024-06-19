package com.suitebde.models.clubs

import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserInClubPayload(
    @PayloadProperty("user")
    @Schema("Id of the user to add", "abc123")
    val userId: UUID,
)
