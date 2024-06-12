package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.repositories.users.IUsersRepository

class UpdateUserLastLoginUseCase(
    private val repository: IUsersRepository,
) : IUpdateUserLastLoginUseCase {

    override suspend fun invoke(input: String): Boolean = repository.updateLastLogin(input)

}
