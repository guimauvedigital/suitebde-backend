package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable

@Serializable
data class UpdateWebMenuPayload(
    val title: String,
    val url: String,
    val position: Int,
)

