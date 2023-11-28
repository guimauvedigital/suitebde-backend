package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty

@Serializable
data class WebMenu(
    @ModelProperty("id")
    override val id: String,
    val associationId: String,
    @ModelProperty("string")
    val title: String,
    @ModelProperty("string")
    val url: String,
    val position: Int = 0,
    val parentMenuId: String? = null,
    val children: List<WebMenu> = emptyList()
) : IChildModel<String, CreateWebMenuPayload, UpdateWebMenuPayload, String> {

    override val parentId: String
        get() = associationId

    val short: String
        get() = title.singleOrNull()?.toString() ?: ""

}
