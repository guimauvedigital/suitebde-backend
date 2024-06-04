package me.nathanfallet.suitebde.usecases.notifications

import com.google.firebase.messaging.FirebaseMessagingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.services.firebase.IFirebaseService

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
