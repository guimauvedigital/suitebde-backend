package me.nathanfallet.suitebde.models.associations

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty

@Serializable
data class UpdateAssociationPayload(
    @PayloadProperty("string", "12")
    val name: String,
    @PayloadProperty("string", "6")
    val school: String,
    @PayloadProperty("string", "6")
    val city: String,
    @PayloadProperty("boolean", "12")
    val validated: Boolean
)
