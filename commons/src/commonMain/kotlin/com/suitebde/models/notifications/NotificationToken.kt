package com.suitebde.models.notifications

import dev.kaccelero.annotations.Schema
import dev.kaccelero.models.IChildModel
import dev.kaccelero.models.UUID
import kotlinx.serialization.Serializable

@Serializable
data class NotificationToken(
    @Schema("FCM Token", "abc123")
    val token: String,
    @Schema("Id of the user owning the token", "abc123")
    val userId: UUID,
) : IChildModel<String, CreateNotificationTokenPayload, Unit, UUID> {

    override val id: String
        get() = token

    override val parentId: UUID
        get() = userId

}
