package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty

@Serializable
data class UpdateWebPagePayload(
    @PayloadProperty("string")
    val url: String,
    @PayloadProperty("string")
    val title: String,
    @PayloadProperty("string")
    val content: String,
    @PayloadProperty("boolean")
    val home: Boolean
)
