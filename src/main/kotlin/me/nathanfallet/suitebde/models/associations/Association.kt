package me.nathanfallet.suitebde.models.associations

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Association(
    val id: String,
    val name: String,
    val school: String,
    val city: String,
    val validated: Boolean,
    val createdAt: Instant,
    val expiresAt: Instant
)
