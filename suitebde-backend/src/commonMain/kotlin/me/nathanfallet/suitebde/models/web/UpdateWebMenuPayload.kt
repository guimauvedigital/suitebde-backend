package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty

@Serializable
data class UpdateWebMenuPayload(
    @PayloadProperty("string")
    val title: String,
    @PayloadProperty("string")
    val url: String,
    @PayloadProperty("integer")
    val position: Int,
)

