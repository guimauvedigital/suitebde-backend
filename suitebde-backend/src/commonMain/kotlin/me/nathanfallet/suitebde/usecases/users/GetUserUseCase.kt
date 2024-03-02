package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase

class GetUserUseCase(
    private val repository: IUsersRepository,
    private val listSubscriptionsInUsersUseCase: IListChildModelSuspendUseCase<SubscriptionInUser, String>,
) : IGetUserUseCase {

    override suspend fun invoke(input: String): User? =
        repository.get(input)?.copy(
            subscriptions = listSubscriptionsInUsersUseCase(input)
        )

}
