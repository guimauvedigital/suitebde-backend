package me.nathanfallet.suitebde.controllers.auth

import io.ktor.server.application.*
import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.models.auth.LoginPayload
import java.util.*

interface IAuthController {

    suspend fun login(payload: LoginPayload, call: ApplicationCall)

    suspend fun join(payload: JoinPayload, joiningAt: Instant, locale: Locale)
    suspend fun join(code: String, joiningAt: Instant): JoinPayload
    suspend fun join(payload: JoinCodePayload, joiningAt: Instant)

}