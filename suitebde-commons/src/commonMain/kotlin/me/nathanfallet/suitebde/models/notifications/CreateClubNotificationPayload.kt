package me.nathanfallet.suitebde.models.notifications

import kotlinx.serialization.Serializable

@Serializable
data class CreateClubNotificationPayload(
    val clubId: String,
    val title: String,
    val body: String,
    val titleArgs: List<String>? = null,
    val bodyArgs: List<String>? = null,
    val data: Map<String, String>? = null,
)
