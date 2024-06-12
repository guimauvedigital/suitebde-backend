package me.nathanfallet.suitebde.usecases.users

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository

class ListUsersLastLoggedBeforeUseCase(
    private val repository: IUsersRepository,
) : IListUsersLastLoggedBeforeUseCase {

    override suspend fun invoke(input: Instant): List<User> = repository.listLastLoggedBefore(input)

}
