package com.suitebde.models.web

import dev.kaccelero.annotations.PayloadProperty
import dev.kaccelero.annotations.Schema
import kotlinx.serialization.Serializable

@Serializable
data class UpdateWebMenuPayload(
    @PayloadProperty("string")
    @Schema("Title of the menu", "Accueil")
    val title: String,
    @PayloadProperty("string")
    @Schema("URL of the menu", "/")
    val url: String,
    @PayloadProperty("integer")
    @Schema("Position of the menu in the menu bar", "0")
    val position: Int,
)

