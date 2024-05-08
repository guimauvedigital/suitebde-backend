package me.nathanfallet.suitebde.models.clubs

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class CreateUserInClubPayload(
    @PayloadProperty("user")
    @Schema("Id of the user to add", "abc123")
    val userId: String,
)
