package com.suitebde.models.stripe

import kotlinx.serialization.Serializable

@Serializable
data class UpdateStripeAccountPayload(
    val chargesEnabled: Boolean,
)
