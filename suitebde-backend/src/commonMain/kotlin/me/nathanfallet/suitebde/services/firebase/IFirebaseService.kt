package me.nathanfallet.suitebde.services.firebase

import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload

interface IFirebaseService {

    suspend fun sendNotification(payload: CreateNotificationPayload)

}
