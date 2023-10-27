package me.nathanfallet.suitebde.controllers.auth

import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User

interface IAuthController {

    suspend fun login(payload: LoginPayload): User

    suspend fun join(payload: JoinPayload)
    suspend fun join(code: String): JoinPayload
    suspend fun join(payload: JoinCodePayload)

}