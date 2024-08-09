package com.suitebde.models.users

import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserPayload(
    @PayloadProperty("string", "6")
    @Schema("First name of the User", "Nathan")
    val firstName: String? = null,
    @PayloadProperty("string", "6")
    @Schema("Last name of the User", "Fallet")
    val lastName: String? = null,
    @PayloadProperty("password", "12")
    val password: String? = null,
)
