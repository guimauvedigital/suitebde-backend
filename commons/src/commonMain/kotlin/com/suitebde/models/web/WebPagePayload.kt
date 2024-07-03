package com.suitebde.models.web

import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import kotlinx.serialization.Serializable

@Serializable
data class WebPagePayload(
    @PayloadProperty("url_webpages")
    @Schema("URL of the page (after /pages/)", "home")
    val url: String,
    @PayloadProperty("string")
    @Schema("Title of the page", "Accueil")
    val title: String,
    @PayloadProperty("markdown")
    @Schema("Content of the page", "...")
    val content: String,
    @PayloadProperty("boolean")
    @Schema("Is the page the home page of the association?", "true")
    val home: Boolean,
)
