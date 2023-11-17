package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase

class CreateUserUseCase(
    private val repository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase
) : ICreateChildModelSuspendUseCase<User, CreateUserPayload, String> {

    override suspend fun invoke(input1: CreateUserPayload, input2: String): User? {
        return repository.create(
            input1.copy(
                password = hashPasswordUseCase(input1.password)
            ),
            input2
        )
    }

}
