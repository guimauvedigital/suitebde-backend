package me.nathanfallet.suitebde.models.associations

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CodeInEmail(
    val email: String,
    val code: String,
    val associationId: String?,
    val expiresAt: Instant
)