package me.nathanfallet.suitebde.models.users

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserPayload(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val superuser: Boolean
)
