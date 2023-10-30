package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.auth.RegisterCodePayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository

class RegisterUseCase(
    private val repository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase
) : IRegisterUseCase {

    override suspend fun invoke(input: RegisterCodePayload): User? {
        return repository.createUser(
            input.associationId,
            input.email,
            hashPasswordUseCase(input.password),
            input.firstName,
            input.lastName,
            false
        )
    }

}