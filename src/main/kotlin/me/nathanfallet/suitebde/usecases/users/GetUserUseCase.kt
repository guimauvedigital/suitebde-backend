package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository

class GetUserUseCase(
    private val repository: IUsersRepository
) : IGetUserUseCase {

    override suspend fun invoke(input: String): User? {
        return repository.getUser(input)
    }

}