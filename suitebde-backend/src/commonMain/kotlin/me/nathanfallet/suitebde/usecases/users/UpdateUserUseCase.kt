package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase
import me.nathanfallet.usecases.models.update.IUpdateChildModelSuspendUseCase

class UpdateUserUseCase(
    private val repository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase,
    private val getUserUseCase: IGetUserUseCase,
) : IUpdateChildModelSuspendUseCase<User, String, UpdateUserPayload, String> {

    override suspend fun invoke(input1: String, input2: UpdateUserPayload, input3: String): User? {
        val updatedUser = input2.password?.let {
            input2.copy(password = hashPasswordUseCase(it))
        } ?: input2
        return if (repository.update(input1, updatedUser, input3)) getUserUseCase(input1)
        else null
    }

}
