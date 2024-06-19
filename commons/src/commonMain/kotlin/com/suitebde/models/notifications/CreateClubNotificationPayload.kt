package com.suitebde.models.notifications

import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class CreateClubNotificationPayload(
    val clubId: UUID,
    val title: String,
    val body: String,
    val titleArgs: List<String>? = null,
    val bodyArgs: List<String>? = null,
    val data: Map<String, String>? = null,
)
