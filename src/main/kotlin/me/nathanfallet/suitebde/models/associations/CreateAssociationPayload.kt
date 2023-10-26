package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable

@Serializable
data class CreateAssociationPayload(
    val name: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)
