package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository

class LoginUseCase(
    private val repository: IUsersRepository,
    private val verifyPasswordUseCase: IVerifyPasswordUseCase
) : ILoginUseCase {

    override suspend fun invoke(input: LoginPayload): User? {
        return repository.getUserForEmail(input.email, true)?.takeIf {
            verifyPasswordUseCase(input.password, it.password ?: "")
        }
    }

}