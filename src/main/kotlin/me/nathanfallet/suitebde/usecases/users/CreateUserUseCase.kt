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

    override suspend fun invoke(input1: CreateUserPayload, input2: Instant): User? {
        return repository.createUser(
            input1.associationId,
            input1.email,
            hashPasswordUseCase(input1.password),
            input1.firstName,
            input1.lastName,
            input1.superuser
        )
    }

}