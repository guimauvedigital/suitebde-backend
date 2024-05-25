package me.nathanfallet.suitebde.usecases.notifications

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.models.notifications.CreateUserNotificationPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.suitebde.services.firebase.IFirebaseService
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase

class SendNotificationToUserUseCase(
    private val listTokensUseCase: IListChildModelSuspendUseCase<NotificationToken, String>,
    private val firebaseService: IFirebaseService,
) : ISendNotificationToUserUseCase {

    override suspend fun invoke(input: CreateUserNotificationPayload) {
        CoroutineScope(Job()).launch {
            listTokensUseCase(input.userId).forEach {
                firebaseService.sendNotification(
                    CreateNotificationPayload(
                        it.token,
                        null,
                        input.title,
                        input.body,
                        input.data
                    )
                )
            }
        }
    }

}
