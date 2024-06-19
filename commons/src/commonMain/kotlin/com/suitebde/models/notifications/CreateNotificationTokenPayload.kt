package com.suitebde.models.notifications

import dev.kaccelero.annotations.Schema
import kotlinx.serialization.Serializable

@Serializable
data class CreateNotificationTokenPayload(
    @Schema("FCM Token", "abc123")
    val token: String,
)
