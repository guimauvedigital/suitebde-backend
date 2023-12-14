package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class CreateDomainInAssociationPayload(
    @PayloadProperty("string")
    @Schema("Domain name linked to the association", "bdensisa.org")
    val domain: String,
)
