package com.suitebde.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterPayload(
    val email: String,
)
