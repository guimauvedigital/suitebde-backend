package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.exceptions.InvalidCredentialsException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository

class LoginUseCase(
    private val repository: IUsersRepository,
    private val verifyPasswordUseCase: IVerifyPasswordUseCase
) : ILoginUseCase {

    override suspend fun invoke(input: Triple<String, String, String>): User {
        return repository.getUserForEmailInAssociation(input.first, input.second, true)?.takeIf {
            verifyPasswordUseCase(Pair(input.third, it.password ?: ""))
        } ?: throw InvalidCredentialsException()
    }

}