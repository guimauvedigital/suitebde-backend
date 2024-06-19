package com.suitebde.models.associations

import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAssociationPayload(
    @PayloadProperty("string", "12")
    @Schema("Name of the association", "BDE de l'ENSISA")
    val name: String,
    @PayloadProperty("string", "6")
    @Schema("Name of the school the association is in", "ENSISA")
    val school: String,
    @PayloadProperty("string", "6")
    @Schema("City of the school the association is in", "Mulhouse")
    val city: String,
    @PayloadProperty("boolean", "12")
    @Schema("Is the association validated by the Suite BDE team?", "true")
    val validated: Boolean,
)
