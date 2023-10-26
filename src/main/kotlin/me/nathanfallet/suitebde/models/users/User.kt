package me.nathanfallet.suitebde.models.users

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val associationId: String,
    val email: String,
    val password: String?,
    val firstName: String,
    val lastName: String,
    val superuser: Boolean
)
