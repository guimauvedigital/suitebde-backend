package com.suitebde.models.notifications

import kotlinx.serialization.Serializable

@Serializable
data class CreateNotificationPayload(
    val token: String? = null,
    val topic: String? = null,
    val title: String,
    val body: String,
    val titleArgs: List<String>? = null,
    val bodyArgs: List<String>? = null,
    val data: Map<String, String>? = null,
)
