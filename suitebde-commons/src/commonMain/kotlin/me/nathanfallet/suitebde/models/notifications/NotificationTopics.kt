package me.nathanfallet.suitebde.models.notifications

import kotlinx.serialization.Serializable

@Serializable
data class NotificationTopics(
    val topics: Map<String, String>,
)
