package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.services.firebase.IFirebaseService

class SendNotificationUseCase(
    private val firebaseService: IFirebaseService,
) : ISendNotificationUseCase {

    override suspend fun invoke(input1: String, input2: CreateNotificationPayload): Boolean {
        firebaseService.sendNotification(input2)
        return true
    }

}
