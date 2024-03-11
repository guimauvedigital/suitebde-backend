package me.nathanfallet.suitebde.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class JoinCodePayload(
    val name: String,
    val school: String,
    val city: String,
    val password: String,
    val firstName: String,
    val lastName: String,
)
