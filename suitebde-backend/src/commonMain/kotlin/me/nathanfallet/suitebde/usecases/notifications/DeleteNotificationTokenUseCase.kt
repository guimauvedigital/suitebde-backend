package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.repositories.notifications.INotificationTokensRepository

class DeleteNotificationTokenUseCase(
    private val repository: INotificationTokensRepository,
) : IDeleteNotificationTokenUseCase {

    override suspend fun invoke(input: String): Boolean = repository.delete(input)

}
