package me.nathanfallet.suitebde.models.web

import kotlinx.serialization.Serializable

@Serializable
data class CreateWebMenuPayload(
    val title: String,
    val url: String,
    val position: Int? = null,
)
