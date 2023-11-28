package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty

@Serializable
data class CreateWebMenuPayload(
    @PayloadProperty("string")
    val title: String,
    @PayloadProperty("string")
    val url: String,
    val position: Int? = null,
)
