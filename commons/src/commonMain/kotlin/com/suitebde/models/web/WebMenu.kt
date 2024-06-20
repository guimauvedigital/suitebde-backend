package com.suitebde.models.web

import dev.kaccelero.annotations.ModelProperty
import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class WebMenu(
    @ModelProperty("id")
    @Schema("Id of the menu", "123abc")
    override val id: UUID,
    @Schema("Id of the association the menu is in", "123abc")
    val associationId: UUID,
    @ModelProperty("string")
    @Schema("Title of the menu", "Accueil")
    val title: String,
    @ModelProperty("string")
    @Schema("URL of the menu", "/")
    val url: String,
    @Schema("Position of the menu in the menu bar", "0")
    val position: Int = 0,
    @Schema("Id of the parent menu", "123abc")
    val parentMenuId: UUID? = null,
    @Schema("List of children menus", "[]")
    val children: List<WebMenu> = emptyList(),
) : IChildModel<UUID, CreateWebMenuPayload, UpdateWebMenuPayload, UUID> {

    override val parentId: UUID
        get() = associationId

    val short = title.singleOrNull()?.toString() ?: ""

}
