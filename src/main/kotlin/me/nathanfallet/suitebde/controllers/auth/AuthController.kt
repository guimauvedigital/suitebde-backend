package me.nathanfallet.suitebde.controllers.auth

import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.ILoginUseCase

class AuthController(
    private val loginUseCase: ILoginUseCase
) : IAuthController {

    override suspend fun login(payload: LoginPayload): User {
        return loginUseCase(payload)
    }

}