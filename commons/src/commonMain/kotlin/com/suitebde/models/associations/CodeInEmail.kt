package com.suitebde.models.associations

import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CodeInEmail(
    val email: String,
    val code: String,
    val associationId: UUID?,
    val expiresAt: Instant,
)
