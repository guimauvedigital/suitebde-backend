package me.nathanfallet.suitebde.controllers.auth

import me.nathanfallet.suitebde.database.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User

interface IAuthController {

    suspend fun login(payload: LoginPayload): User

}