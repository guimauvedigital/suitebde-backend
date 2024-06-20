package com.suitebde.models.stripe

import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class StripeAccount(
    val associationId: UUID,
    val accountId: String,
    val chargesEnabled: Boolean,
) : IChildModel<String, CreateStripeAccountPayload, UpdateStripeAccountPayload, UUID> {

    override val id: String
        get() = accountId

    override val parentId: UUID
        get() = associationId

}
