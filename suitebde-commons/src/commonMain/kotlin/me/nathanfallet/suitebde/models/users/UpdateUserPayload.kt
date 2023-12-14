package me.nathanfallet.suitebde.models.users

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class UpdateUserPayload(
    @PayloadProperty("string", "6")
    @Schema("First name of the User", "Nathan")
    val firstName: String?,
    @PayloadProperty("string", "6")
    @Schema("Last name of the User", "Fallet")
    val lastName: String?,
    @PayloadProperty("password", "12")
    val password: String?,
)
