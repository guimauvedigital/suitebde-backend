package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class WebPage(
    @ModelProperty("id")
    @Schema("Id of the page", "123abc")
    override val id: String,
    @Schema("Id of the association the page is in", "123abc")
    val associationId: String,
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
) : IChildModel<String, WebPagePayload, WebPagePayload, String> {

    override val parentId: String
        get() = associationId

}
