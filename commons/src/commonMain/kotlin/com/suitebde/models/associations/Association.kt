package com.suitebde.models.associations

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IModel
import dev.kaccelero.models.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Association(
    @ModelProperty("id")
    @Schema("Id of the association", "123abc")
    override val id: UUID,
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
) : IModel<UUID, CreateAssociationPayload, UpdateAssociationPayload>
