package com.suitebde.usecases.notifications

import com.google.firebase.messaging.FirebaseMessagingException
import com.suitebde.models.notifications.CreateNotificationPayload
import com.suitebde.services.firebase.IFirebaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SendNotificationUseCase(
    private val firebaseService: IFirebaseService,
    private val deleteNotificationTokenUseCase: IDeleteNotificationTokenUseCase,
) : ISendNotificationUseCase {

    override suspend fun invoke(input: CreateNotificationPayload) {
        CoroutineScope(Job()).launch {
            try {
                firebaseService.sendNotification(input)
            } catch (e: FirebaseMessagingException) {
                input.token?.let { deleteNotificationTokenUseCase(it) }
            }
        }
    }

}
