package me.nathanfallet.suitebde.models.users

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserPayload(
    val firstName: String?,
    val lastName: String?,
    val password: String?
)
