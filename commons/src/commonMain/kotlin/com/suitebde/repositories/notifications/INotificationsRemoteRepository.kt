package com.suitebde.repositories.notifications

import com.suitebde.models.notifications.CreateNotificationPayload
import com.suitebde.models.notifications.NotificationTopics
import dev.kaccelero.models.UUID

interface INotificationsRemoteRepository {

    suspend fun send(payload: CreateNotificationPayload, associationId: UUID)
    suspend fun topics(associationId: UUID): NotificationTopics

}
