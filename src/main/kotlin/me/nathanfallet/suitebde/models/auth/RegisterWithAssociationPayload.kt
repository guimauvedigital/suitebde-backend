package me.nathanfallet.suitebde.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterWithAssociationPayload(
    val email: String,
    val association: String
)
