package com.suitebde.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenPayload(
    val refreshToken: String,
)
