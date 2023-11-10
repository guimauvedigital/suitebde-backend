package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase
import me.nathanfallet.usecases.models.update.IUpdateModelSuspendUseCase

class UpdateUserUseCase(
    private val repository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase
) : IUpdateModelSuspendUseCase<User, String, UpdateUserPayload> {

    override suspend fun invoke(input1: String, input2: UpdateUserPayload): User? {
        val updatedUser = input2.password?.let {
            input2.copy(password = hashPasswordUseCase(it))
        } ?: input2
        return if (repository.update(input1, updatedUser)) repository.get(input1)
        else null
    }

}