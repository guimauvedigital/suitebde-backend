package me.nathanfallet.suitebde.models.notifications

import kotlinx.serialization.Serializable

@Serializable
data class CreateNotificationPayload(
    val token: String? = null,
    val topic: String? = null,
    val title: String,
    val body: String,
    val data: Map<String, String>? = null,
)
