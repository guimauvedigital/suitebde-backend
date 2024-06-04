package me.nathanfallet.suitebde.usecases.notifications

import com.google.firebase.messaging.FirebaseMessagingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.models.notifications.CreateClubNotificationPayload
import me.nathanfallet.suitebde.models.notifications.CreateNotificationPayload
import me.nathanfallet.suitebde.repositories.notifications.INotificationTokensRepository
import me.nathanfallet.suitebde.services.firebase.IFirebaseService

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
