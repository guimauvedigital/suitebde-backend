package com.suitebde.models.stripe

import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class StripeOrder(
    val sessionId: String,
    val associationId: UUID,
    val email: String,
    val items: List<CheckoutItem>,
    val paidAt: Instant?,
) : IChildModel<String, CreateStripeOrderPayload, UpdateStripeOrderPayload, UUID> {

    override val id: String
        get() = sessionId

    override val parentId: UUID
        get() = associationId

}
