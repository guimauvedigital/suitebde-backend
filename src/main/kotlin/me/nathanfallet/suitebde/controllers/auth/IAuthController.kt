package me.nathanfallet.suitebde.controllers.auth

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User
import java.util.*

interface IAuthController {

    suspend fun login(payload: LoginPayload): User

    suspend fun join(payload: JoinPayload, joiningAt: Instant, locale: Locale)
    suspend fun join(code: String, joiningAt: Instant): JoinPayload
    suspend fun join(payload: JoinCodePayload, joiningAt: Instant)

}