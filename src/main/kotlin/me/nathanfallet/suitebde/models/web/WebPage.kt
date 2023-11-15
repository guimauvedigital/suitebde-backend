package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty

@Serializable
data class WebPage(
    @ModelProperty("id")
    override val id: String,
    val associationId: String,
    @ModelProperty("url_webpages")
    val url: String,
    @ModelProperty("string")
    val title: String,
    val content: String,
    @ModelProperty("boolean")
    val home: Boolean = false,
) : IChildModel<String, CreateWebPagePayload, UpdateWebPagePayload, String> {

    override val parentId: String
        get() = associationId

}
