package com.suitebde.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginPayload(
    val email: String,
    val password: String,
)
