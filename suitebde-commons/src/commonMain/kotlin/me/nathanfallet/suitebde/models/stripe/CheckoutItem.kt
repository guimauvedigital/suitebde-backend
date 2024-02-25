package me.nathanfallet.suitebde.models.stripe

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutItem(
    val itemType: String,
    val itemId: String,
    val name: String,
    val description: String,
    val quantity: Long,
    val unitAmount: Long,
    val currency: String = "eur",
)
