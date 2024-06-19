package com.suitebde.repositories.notifications

import com.suitebde.models.notifications.CreateNotificationTokenPayload
import com.suitebde.models.notifications.NotificationToken
import dev.kaccelero.models.UUID

interface INotificationTokensRemoteRepository {

    suspend fun create(
        payload: CreateNotificationTokenPayload,
        userId: UUID,
        associationId: UUID,
    ): NotificationToken?

}
