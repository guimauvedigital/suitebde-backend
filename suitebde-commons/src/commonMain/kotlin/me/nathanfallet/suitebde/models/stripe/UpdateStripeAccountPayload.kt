package me.nathanfallet.suitebde.models.stripe

import kotlinx.serialization.Serializable

@Serializable
data class UpdateStripeAccountPayload(
    val chargesEnabled: Boolean,
)
