package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class WebPagePayload(
    @PayloadProperty("url_webpages")
    @Schema("URL of the page (after /pages/)", "home")
    val url: String,
    @PayloadProperty("string")
    @Schema("Title of the page", "Accueil")
    val title: String,
    @PayloadProperty("string")
    @Schema("Content of the page", "...")
    val content: String,
    @PayloadProperty("boolean")
    @Schema("Is the page the home page of the association?", "true")
    val home: Boolean,
)
