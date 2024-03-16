package me.nathanfallet.suitebde.services.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload

class FirebaseService : IFirebaseService {

    private var initialized: Boolean

    init {
        try {
            FirebaseApp.initializeApp()
            initialized = true
        } catch (e: Exception) {
            initialized = false
        }
    }

    override suspend fun sendNotification(payload: CreateNotificationPayload) {
        if (!initialized) return
        FirebaseMessaging.getInstance().send(
            Message.builder().apply {
                payload.token?.let { setToken(it) }
                payload.topic?.let { setTopic(it) }

                setNotification(
                    Notification.builder()
                        .setTitle(payload.title)
                        .setBody(payload.body)
                        .build()
                )
                putAllData(payload.data)
            }.build()
        )
    }

}
