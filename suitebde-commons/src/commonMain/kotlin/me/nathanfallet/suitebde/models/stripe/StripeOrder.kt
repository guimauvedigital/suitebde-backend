package me.nathanfallet.suitebde.models.stripe

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel

@Serializable
data class StripeOrder(
    val sessionId: String,
    val associationId: String,
    val email: String,
    val items: List<CheckoutItem>,
    val paidAt: Instant?,
) : IChildModel<String, CreateStripeOrderPayload, UpdateStripeOrderPayload, String> {

    override val id: String
        get() = sessionId

    override val parentId: String
        get() = associationId

}
