package me.nathanfallet.suitebde.usecases.auth

import io.ktor.http.*
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository

class LoginUseCase(
    private val repository: IUsersRepository,
    private val verifyPasswordUseCase: IVerifyPasswordUseCase
) : ILoginUseCase {

    override suspend fun invoke(input: LoginPayload): User {
        return repository.getUserForEmail(input.email, true)?.takeIf {
            verifyPasswordUseCase(Pair(input.password, it.password ?: ""))
        } ?: throw ControllerException(HttpStatusCode.Unauthorized, LocalizedString.AUTH_INVALID_CREDENTIALS)
    }

}