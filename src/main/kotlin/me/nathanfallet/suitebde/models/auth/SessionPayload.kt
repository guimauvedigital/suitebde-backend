package me.nathanfallet.suitebde.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class SessionPayload(
    val userId: String
)
