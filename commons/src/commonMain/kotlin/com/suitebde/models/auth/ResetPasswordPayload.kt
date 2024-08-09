package com.suitebde.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordPayload(
    val password: String,
)
