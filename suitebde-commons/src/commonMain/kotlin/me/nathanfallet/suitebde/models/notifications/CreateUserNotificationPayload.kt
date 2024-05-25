package me.nathanfallet.suitebde.models.notifications

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserNotificationPayload(
    val userId: String,
    val title: String,
    val body: String,
    val data: Map<String, String>? = null,
)
