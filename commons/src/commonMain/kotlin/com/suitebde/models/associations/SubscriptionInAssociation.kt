package com.suitebde.models.associations

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionInAssociation(
    @ModelProperty("id")
    @Schema("Id of the subscription", "123abc")
    override val id: UUID,
    @Schema("Id of the association the subscription is in", "123abc")
    val associationId: UUID,
    @ModelProperty("string")
    @Schema("Name of the subscription", "Cotisation annuelle")
    val name: String,
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
) : IChildModel<UUID, CreateSubscriptionInAssociationPayload, UpdateSubscriptionInAssociationPayload, UUID> {

    override val parentId: UUID
        get() = associationId

}
