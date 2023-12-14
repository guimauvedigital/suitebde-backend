package me.nathanfallet.suitebde.models.associations

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class Association(
    @ModelProperty("id")
    @Schema("Id of the association", "123abc")
    override val id: String,
    @ModelProperty("string")
    @Schema("Name of the association", "BDE de l'ENSISA")
    val name: String,
    @ModelProperty("string")
    @Schema("Name of the school the association is in", "ENSISA")
    val school: String,
    @ModelProperty("string")
    @Schema("City of the school the association is in", "Mulhouse")
    val city: String,
    @ModelProperty("boolean")
    @Schema("Is the association validated by the Suite BDE team?", "true")
    val validated: Boolean,
    @Schema("Creation date of the association", "2023-12-14T09:41:00Z")
    val createdAt: Instant,
    @Schema("Expiration date of the validity of association", "2023-12-14T09:41:00Z")
    val expiresAt: Instant,
) : IModel<String, CreateAssociationPayload, UpdateAssociationPayload>
