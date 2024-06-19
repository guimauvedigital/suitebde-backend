package com.suitebde.models.associations

import kotlinx.serialization.Serializable

@Serializable
data class CreateAssociationPayload(
    val name: String,
    val school: String,
    val city: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
)
