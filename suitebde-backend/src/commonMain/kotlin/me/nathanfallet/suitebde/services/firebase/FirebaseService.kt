package me.nathanfallet.suitebde.services.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.*
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
                payload.data?.let { putAllData(it) }
                setApnsConfig(ApnsConfig.builder().apply {
                    setAps(Aps.builder().apply {
                        setAlert(ApsAlert.builder().apply {
                            setTitleLocalizationKey(payload.title)
                            setLocalizationKey(payload.body)
                            payload.titleArgs?.let { addAllTitleLocArgs(it) }
                            payload.bodyArgs?.let { addAllLocalizationArgs(it) }
                        }.build())
                    }.build())
                }.build())
                setAndroidConfig(AndroidConfig.builder().apply {
                    setNotification(AndroidNotification.builder().apply {
                        setTitleLocalizationKey(payload.title)
                        setBodyLocalizationKey(payload.body)
                        payload.titleArgs?.let { addAllTitleLocalizationArgs(it) }
                        payload.bodyArgs?.let { addAllBodyLocalizationArgs(it) }
                    }.build())
                }.build())
            }.build()
        )
    }

}
