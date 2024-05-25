package me.nathanfallet.suitebde.usecases.notifications

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.services.firebase.IFirebaseService

class SendNotificationUseCase(
    private val firebaseService: IFirebaseService,
) : ISendNotificationUseCase {

    override suspend fun invoke(input: CreateNotificationPayload) {
        CoroutineScope(Job()).launch {
            firebaseService.sendNotification(input)
        }
    }

}
