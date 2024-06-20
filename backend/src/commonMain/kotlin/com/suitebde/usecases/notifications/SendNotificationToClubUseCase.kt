package com.suitebde.usecases.notifications

import com.google.firebase.messaging.FirebaseMessagingException
import com.suitebde.models.notifications.CreateClubNotificationPayload
import com.suitebde.models.notifications.CreateNotificationPayload
import com.suitebde.repositories.notifications.INotificationTokensRepository
import com.suitebde.services.firebase.IFirebaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SendNotificationToClubUseCase(
    private val repository: INotificationTokensRepository,
    private val firebaseService: IFirebaseService,
    private val deleteNotificationTokenUseCase: IDeleteNotificationTokenUseCase,
) : ISendNotificationToClubUseCase {

    override suspend fun invoke(input: CreateClubNotificationPayload) {
        CoroutineScope(Job()).launch {
            repository.listByClub(input.clubId).forEach {
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
