package me.nathanfallet.suitebde.models.notifications

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class CreateNotificationTokenPayload(
    @Schema("FCM Token", "abc123")
    val token: String,
)
