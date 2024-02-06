package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class SubscriptionInAssociation(
    @ModelProperty("id")
    @Schema("Id of the subscription", "123abc")
    override val id: String,
    @Schema("Id of the association the subscription is in", "123abc")
    val associationId: String,
    @ModelProperty("string")
    @Schema("Name of the subscription", "Cotisation annuelle")
    val name: String,
    @ModelProperty("string")
    @Schema("Description of the subscription", "Une cotisation annuelle pour l'association")
    val description: String,
    @ModelProperty("price")
    @Schema("Price of the subscription", "10.0")
    val price: Double,
    @ModelProperty("duration")
    @Schema("Duration of the subscription", "1y")
    val duration: String,
    @ModelProperty("boolean")
    @Schema("Is the subscription auto renewable?", "false")
    val autoRenewable: Boolean,
) : IChildModel<String, CreateSubscriptionInAssociationPayload, UpdateSubscriptionInAssociationPayload, String> {

    override val parentId: String
        get() = associationId

}
