package com.suitebde.usecases.notifications

import com.google.firebase.messaging.FirebaseMessagingException
import com.suitebde.models.notifications.CreateNotificationPayload
import com.suitebde.models.notifications.CreateUserNotificationPayload
import com.suitebde.repositories.notifications.INotificationTokensRepository
import com.suitebde.services.firebase.IFirebaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SendNotificationToUserUseCase(
    private val repository: INotificationTokensRepository,
    private val firebaseService: IFirebaseService,
    private val deleteNotificationTokenUseCase: IDeleteNotificationTokenUseCase,
) : ISendNotificationToUserUseCase {

    override suspend fun invoke(input: CreateUserNotificationPayload) {
        CoroutineScope(Job()).launch {
            repository.list(input.userId).forEach {
                try {
                    firebaseService.sendNotification(
                        CreateNotificationPayload(
                            it.token,
                            null,
                            input.title,
                            input.body,
                            input.titleArgs,
                            input.bodyArgs,
                            input.data
                        )
                    )
                } catch (e: FirebaseMessagingException) {
                    deleteNotificationTokenUseCase(it.token)
                }
            }
        }
    }

}
