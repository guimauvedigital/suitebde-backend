package me.nathanfallet.suitebde.models.roles

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserInRolePayload(
    val userId: String,
)
