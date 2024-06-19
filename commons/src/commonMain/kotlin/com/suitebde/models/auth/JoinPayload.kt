package com.suitebde.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class JoinPayload(
    val email: String,
)
