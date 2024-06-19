package com.suitebde.usecases.notifications

import com.suitebde.repositories.notifications.INotificationTokensRepository

class DeleteNotificationTokenUseCase(
    private val repository: INotificationTokensRepository,
) : IDeleteNotificationTokenUseCase {

    override suspend fun invoke(input: String): Boolean = repository.delete(input)

}
