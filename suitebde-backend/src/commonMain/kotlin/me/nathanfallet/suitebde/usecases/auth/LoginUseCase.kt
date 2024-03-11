package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.users.IGetUserForEmailUseCase

class LoginUseCase(
    private val getUserForEmailUseCase: IGetUserForEmailUseCase,
    private val verifyPasswordUseCase: IVerifyPasswordUseCase,
) : ILoginUseCase {

    override suspend fun invoke(input: LoginPayload): User? {
        return getUserForEmailUseCase(input.email, true)?.takeIf {
            verifyPasswordUseCase(input.password, it.password ?: "")
        }
    }

}
