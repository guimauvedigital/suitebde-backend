package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable
import me.nathanfallet.suitebde.usecases.application.AddDurationUseCase
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema
import me.nathanfallet.usecases.models.annotations.validators.StringPropertyValidator

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
