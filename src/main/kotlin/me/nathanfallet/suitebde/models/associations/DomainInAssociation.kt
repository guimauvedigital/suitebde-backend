package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable

@Serializable
data class DomainInAssociation(
    val domain: String,
    val associationId: String
)
