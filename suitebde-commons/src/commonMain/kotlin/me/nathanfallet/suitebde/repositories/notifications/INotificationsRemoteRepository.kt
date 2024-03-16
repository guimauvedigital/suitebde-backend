package me.nathanfallet.suitebde.repositories.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.models.notifications.NotificationTopics

interface INotificationsRemoteRepository {

    suspend fun send(payload: CreateNotificationPayload, associationId: String)
    suspend fun topics(associationId: String): NotificationTopics

}
