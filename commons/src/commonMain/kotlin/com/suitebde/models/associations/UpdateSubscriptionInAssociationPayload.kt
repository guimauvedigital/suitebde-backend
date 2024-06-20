package com.suitebde.models.associations

import com.suitebde.usecases.application.AddDurationUseCase
import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.annotations.StringPropertyValidator
import kotlinx.serialization.Serializable

@Serializable
data class UpdateSubscriptionInAssociationPayload(
    @PayloadProperty("string")
    @Schema("Name of the subscription", "Cotisation annuelle")
    val name: String? = null,
    @PayloadProperty("string")
    @Schema("Description of the subscription", "Une cotisation annuelle pour l'association")
    val description: String? = null,
    @PayloadProperty("price")
    @Schema("Price of the subscription", "10.0")
    val price: Double? = null,
    @PayloadProperty("duration")
    @Schema("Duration of the subscription", "1y")
    @StringPropertyValidator(AddDurationUseCase.DURATION_REGEX)
    val duration: String? = null,
    @PayloadProperty("boolean")
    @Schema("Is the subscription auto renewable?", "false")
    val autoRenewable: Boolean? = null,
)
