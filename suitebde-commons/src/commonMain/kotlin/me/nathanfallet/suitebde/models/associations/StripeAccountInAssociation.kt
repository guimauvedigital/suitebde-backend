package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel

@Serializable
data class StripeAccountInAssociation(
    val associationId: String,
    val accountId: String,
    val chargesEnabled: Boolean,
) : IChildModel<String, CreateStripeAccountInAssociationPayload, UpdateStripeAccountInAssociationPayload, String> {

    override val id: String
        get() = accountId

    override val parentId: String
        get() = associationId

}
