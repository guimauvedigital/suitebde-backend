package me.nathanfallet.suitebde.models.notifications

import kotlinx.serialization.Serializable
import me.nathanfallet.usecases.models.IChildModel
import me.nathanfallet.usecases.models.annotations.Schema

@Serializable
data class NotificationToken(
    @Schema("FCM Token", "abc123")
    val token: String,
    @Schema("Id of the user owning the token", "abc123")
    val userId: String,
) : IChildModel<String, CreateNotificationTokenPayload, Unit, String> {

    override val id: String
        get() = token

    override val parentId: String
        get() = userId

}
