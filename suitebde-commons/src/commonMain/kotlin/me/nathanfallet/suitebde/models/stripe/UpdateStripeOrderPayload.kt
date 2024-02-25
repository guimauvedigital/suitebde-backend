package me.nathanfallet.suitebde.models.stripe

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UpdateStripeOrderPayload(
    val paidAt: Instant?,
)
