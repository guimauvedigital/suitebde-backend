package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase

class UpdateUserUseCase(
    private val repository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase
) : IUpdateUserUseCase {

    override suspend fun invoke(input: User): User? {
        val updatedUser = input.password?.let {
            input.copy(password = hashPasswordUseCase(it))
        } ?: input
        return if (repository.updateUser(updatedUser) == 1) updatedUser
        else null
    }

}