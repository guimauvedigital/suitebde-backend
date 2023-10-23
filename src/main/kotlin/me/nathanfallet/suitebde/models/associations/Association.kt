package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable

@Serializable
data class Association(
    val id: String,
    val name: String
)
