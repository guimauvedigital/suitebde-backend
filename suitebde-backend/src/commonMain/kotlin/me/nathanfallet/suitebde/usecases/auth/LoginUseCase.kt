package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.ktorx.usecases.auth.ILoginUseCase
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.usecases.users.IGetUserForEmailUseCase
import me.nathanfallet.usecases.users.IUser

class LoginUseCase(
    private val getUserForEmailUseCase: IGetUserForEmailUseCase,
    private val verifyPasswordUseCase: IVerifyPasswordUseCase,
) : ILoginUseCase<LoginPayload> {

    override suspend fun invoke(input: LoginPayload): IUser? {
        return getUserForEmailUseCase(input.email, true)?.takeIf {
            verifyPasswordUseCase(input.password, it.password ?: "")
        }
    }

}
