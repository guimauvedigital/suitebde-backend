package me.nathanfallet.suitebde.models.stripe

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel

@Serializable
data class StripeAccount(
    val associationId: String,
    val accountId: String,
    val chargesEnabled: Boolean,
) : IChildModel<String, CreateStripeAccountPayload, UpdateStripeAccountPayload, String> {

    override val id: String
        get() = accountId

    override val parentId: String
        get() = associationId

}
