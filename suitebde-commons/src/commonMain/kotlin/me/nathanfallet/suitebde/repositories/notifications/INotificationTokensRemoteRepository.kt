package me.nathanfallet.suitebde.repositories.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationTokenPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken

interface INotificationTokensRemoteRepository {

    suspend fun create(
        payload: CreateNotificationTokenPayload,
        userId: String,
        associationId: String,
    ): NotificationToken?

}
