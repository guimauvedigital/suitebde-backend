package me.nathanfallet.suitebde.models.roles

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserInRole(
    val userId: String,
)
