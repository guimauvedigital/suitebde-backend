package me.nathanfallet.suitebde.models.clubs

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserInClub(
    val userId: String,
)
