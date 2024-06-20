package com.suitebde.usecases.users

import com.suitebde.models.users.User
import com.suitebde.repositories.users.IUsersRepository
import kotlinx.datetime.Instant

class ListUsersLastLoggedBeforeUseCase(
    private val repository: IUsersRepository,
) : IListUsersLastLoggedBeforeUseCase {

    override suspend fun invoke(input: Instant): List<User> = repository.listLastLoggedBefore(input)

}
