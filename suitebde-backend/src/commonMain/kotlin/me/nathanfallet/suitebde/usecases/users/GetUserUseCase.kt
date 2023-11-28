package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository

class GetUserUseCase(
    private val repository: IUsersRepository
) : IGetUserUseCase {

    override suspend fun invoke(input: String): User? {
        return repository.get(input)
    }

}
