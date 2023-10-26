package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository

class UpdateUserUseCase(
    private val repository: IUsersRepository
) : IUpdateUserUseCase {

    override suspend fun invoke(input: User) {
        repository.updateUser(input)
    }

}