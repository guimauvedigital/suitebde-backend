package me.nathanfallet.suitebde.models.users

import kotlinx.serialization.Serializable
import me.nathanfallet.ktor.routers.models.base.PayloadProperty

@Serializable
data class UpdateUserPayload(
    @PayloadProperty("string", "6")
    val firstName: String?,
    @PayloadProperty("string", "6")
    val lastName: String?,
    @PayloadProperty("password", "12")
    val password: String?
)
