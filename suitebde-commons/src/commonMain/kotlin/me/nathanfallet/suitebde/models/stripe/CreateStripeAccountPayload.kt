package me.nathanfallet.suitebde.models.stripe

import kotlinx.serialization.Serializable

@Serializable
data class CreateStripeAccountPayload(
    val accountId: String,
    val chargesEnabled: Boolean,
)
