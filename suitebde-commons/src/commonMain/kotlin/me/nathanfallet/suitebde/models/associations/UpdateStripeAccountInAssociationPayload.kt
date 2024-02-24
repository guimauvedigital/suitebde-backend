package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable

@Serializable
data class UpdateStripeAccountInAssociationPayload(
    val chargesEnabled: Boolean,
)
