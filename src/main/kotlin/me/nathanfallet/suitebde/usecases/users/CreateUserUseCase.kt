package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase

class CreateUserUseCase(
    private val repository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase
) : ICreateModelSuspendUseCase<User, CreateUserPayload> {

    override suspend fun invoke(input: CreateUserPayload): User? {
        return repository.create(
            input.copy(
                password = hashPasswordUseCase(input.password)
            )
        )
    }

}