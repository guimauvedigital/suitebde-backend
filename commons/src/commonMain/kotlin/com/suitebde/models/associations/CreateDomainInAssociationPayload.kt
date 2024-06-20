package com.suitebde.models.associations

import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import kotlinx.serialization.Serializable

@Serializable
data class CreateDomainInAssociationPayload(
    @PayloadProperty("string")
    @Schema("Domain name linked to the association", "bdensisa.org")
    val domain: String,
)
