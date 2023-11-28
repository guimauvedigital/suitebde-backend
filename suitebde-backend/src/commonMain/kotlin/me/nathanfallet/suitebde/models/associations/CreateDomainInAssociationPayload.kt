package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty

@Serializable
data class CreateDomainInAssociationPayload(
    @PayloadProperty("string")
    val domain: String
)
