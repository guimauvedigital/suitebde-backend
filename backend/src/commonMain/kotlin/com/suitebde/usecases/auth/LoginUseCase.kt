package com.suitebde.usecases.auth

import com.suitebde.models.auth.LoginPayload
import com.suitebde.models.users.User
import com.suitebde.usecases.users.IGetUserForEmailUseCase

class LoginUseCase(
    private val getUserForEmailUseCase: IGetUserForEmailUseCase,
    private val verifyPasswordUseCase: IVerifyPasswordUseCase,
) : ILoginUseCase {

    override suspend fun invoke(input: LoginPayload): User? =
        getUserForEmailUseCase(input.email, true)?.takeIf {
            verifyPasswordUseCase(input.password, it.password ?: "")
        }

}
