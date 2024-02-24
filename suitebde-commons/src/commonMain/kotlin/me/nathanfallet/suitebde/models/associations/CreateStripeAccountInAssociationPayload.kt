package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable

@Serializable
data class CreateStripeAccountInAssociationPayload(
    val accountId: String,
    val chargesEnabled: Boolean,
)
