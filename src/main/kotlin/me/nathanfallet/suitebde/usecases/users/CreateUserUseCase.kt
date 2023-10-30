package me.nathanfallet.suitebde.usecases.users

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase

class CreateUserUseCase(
    private val repository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase
) : ICreateUserUseCase {

    override suspend fun invoke(input: Pair<CreateUserPayload, Instant>): User? {
        return repository.createUser(
            input.first.associationId,
            input.first.email,
            hashPasswordUseCase(input.first.password),
            input.first.firstName,
            input.first.lastName,
            input.first.superuser
        )
    }

}