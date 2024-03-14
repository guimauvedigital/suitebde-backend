package me.nathanfallet.suitebde.repositories.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload

interface INotificationsRemoteRepository {

    suspend fun send(payload: CreateNotificationPayload, associationId: String)

}
