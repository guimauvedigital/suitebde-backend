package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.PayloadProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class CreateWebMenuPayload(
    @PayloadProperty("string")
    @Schema("Title of the menu", "Accueil")
    val title: String,
    @PayloadProperty("string")
    @Schema("URL of the menu", "/")
    val url: String,
    @Schema("Position of the menu in the menu bar", "0")
    val position: Int? = null,
)
