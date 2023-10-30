package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable

@Serializable
data class WebMenu(
    val id: String,
    val associationId: String,
    val title: String,
    val url: String,
    val position: Int = 0,
    val parentId: String? = null,
    val children: List<WebMenu> = emptyList()
) {

    val short: String
        get() = title.singleOrNull()?.toString() ?: ""

}
