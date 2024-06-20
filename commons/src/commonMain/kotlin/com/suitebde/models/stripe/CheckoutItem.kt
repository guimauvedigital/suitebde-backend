package com.suitebde.models.stripe

import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutItem(
    val itemType: String,
    val itemId: UUID,
    val name: String,
    val description: String,
    val quantity: Long,
    val unitAmount: Long,
    val currency: String = "eur",
)
