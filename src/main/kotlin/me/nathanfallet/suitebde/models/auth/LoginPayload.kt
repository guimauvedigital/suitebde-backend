package me.nathanfallet.suitebde.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginPayload(
    val email: String,
    val associationId: String,
    val password: String
)
