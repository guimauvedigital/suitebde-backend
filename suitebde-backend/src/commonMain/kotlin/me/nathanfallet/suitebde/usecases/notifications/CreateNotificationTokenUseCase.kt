package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.CreateNotificationTokenPayload
import me.nathanfallet.suitebde.models.notifications.NotificationToken
import me.nathanfallet.suitebde.repositories.notifications.INotificationTokensRepository
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase

class CreateNotificationTokenUseCase(
    private val repository: INotificationTokensRepository,
) : ICreateChildModelSuspendUseCase<NotificationToken, CreateNotificationTokenPayload, String> {

    override suspend fun invoke(input1: CreateNotificationTokenPayload, input2: String): NotificationToken? = try {
        repository.create(input1, input2)
    } catch (e: Exception) {
        repository.get(input1.token, input2)
    }

}
