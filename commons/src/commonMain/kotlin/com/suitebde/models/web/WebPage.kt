package com.suitebde.models.web

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class WebPage(
    @ModelProperty("id")
    @Schema("Id of the page", "123abc")
    override val id: UUID,
    @Schema("Id of the association the page is in", "123abc")
    val associationId: UUID,
    @ModelProperty("url_webpages")
    @Schema("URL of the page (after /pages/)", "home")
    val url: String,
    @ModelProperty("string")
    @Schema("Title of the page", "Accueil")
    val title: String,
    @Schema("Content of the page", "...")
    val content: String,
    @ModelProperty("boolean")
    @Schema("Is the page the home page of the association?", "true")
    val home: Boolean = false,
) : IChildModel<UUID, WebPagePayload, WebPagePayload, UUID> {

    override val parentId: UUID
        get() = associationId

}
