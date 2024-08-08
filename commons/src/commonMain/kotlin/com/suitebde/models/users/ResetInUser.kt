package com.suitebde.models.users

import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ResetInUser(
    val code: String,
    val userId: UUID,
    val expiration: Instant,
)
