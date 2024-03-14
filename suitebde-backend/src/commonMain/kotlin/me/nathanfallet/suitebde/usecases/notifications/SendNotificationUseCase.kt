package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload

class SendNotificationUseCase : ISendNotificationUseCase {

    override suspend fun invoke(input1: String, input2: CreateNotificationPayload): Boolean {
        return false
    }

}
