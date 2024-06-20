package com.suitebde.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterCodePayload(
    val password: String,
    val firstName: String,
    val lastName: String,
)
