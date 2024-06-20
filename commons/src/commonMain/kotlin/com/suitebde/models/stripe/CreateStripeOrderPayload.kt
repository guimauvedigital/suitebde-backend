package com.suitebde.models.stripe

import kotlinx.serialization.Serializable

@Serializable
data class CreateStripeOrderPayload(
    val sessionId: String,
    val email: String,
    val items: List<CheckoutItem>,
)
