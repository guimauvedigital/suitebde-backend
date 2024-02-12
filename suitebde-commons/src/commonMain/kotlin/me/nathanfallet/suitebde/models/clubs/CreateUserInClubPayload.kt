package me.nathanfallet.suitebde.models.clubs

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserInClubPayload(
    val userId: String,
)
