package com.suitebde.services.firebase

import com.suitebde.models.notifications.CreateNotificationPayload

interface IFirebaseService {

    suspend fun sendNotification(payload: CreateNotificationPayload)

}
