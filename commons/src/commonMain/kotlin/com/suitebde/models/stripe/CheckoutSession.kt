package com.suitebde.models.stripe

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutSession(
    val id: String,
    val url: String,
)
