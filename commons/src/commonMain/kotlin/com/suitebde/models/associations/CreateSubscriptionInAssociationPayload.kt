package com.suitebde.models.associations

import com.suitebde.usecases.application.AddDurationUseCase
import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.annotations.StringPropertyValidator
import kotlinx.serialization.Serializable

@Serializable
data class CreateSubscriptionInAssociationPayload(
    @PayloadProperty("string")
    @Schema("Name of the subscription", "Cotisation annuelle")
    val name: String,
    @PayloadProperty("string")
    @Schema("Description of the subscription", "Une cotisation annuelle pour l'association")
    val description: String,
    @PayloadProperty("price")
    @Schema("Price of the subscription", "10.0")
    val price: Double,
    @PayloadProperty("duration")
    @Schema("Duration of the subscription", "1y")
    @StringPropertyValidator(AddDurationUseCase.DURATION_REGEX)
    val duration: String,
    @PayloadProperty("boolean")
    @Schema("Is the subscription auto renewable?", "false")
    val autoRenewable: Boolean,
)
