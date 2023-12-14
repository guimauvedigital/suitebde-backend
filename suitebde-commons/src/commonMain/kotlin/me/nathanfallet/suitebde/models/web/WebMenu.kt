package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.ModelProperty
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class WebMenu(
    @ModelProperty("id")
    @Schema("Id of the menu", "123abc")
    override val id: String,
    @Schema("Id of the association the menu is in", "123abc")
    val associationId: String,
    @ModelProperty("string")
    @Schema("Title of the menu", "Accueil")
    val title: String,
    @ModelProperty("string")
    @Schema("URL of the menu", "/")
    val url: String,
    @Schema("Position of the menu in the menu bar", "0")
    val position: Int = 0,
    @Schema("Id of the parent menu", "123abc")
    val parentMenuId: String? = null,
    @Schema("List of children menus", "[]")
    val children: List<WebMenu> = emptyList(),
) : IChildModel<String, CreateWebMenuPayload, UpdateWebMenuPayload, String> {

    override val parentId: String
        get() = associationId

    val short: String
        get() = title.singleOrNull()?.toString() ?: ""

}
