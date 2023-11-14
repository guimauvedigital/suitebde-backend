package me.nathanfallet.suitebde.controllers.auth

import io.ktor.server.application.*
import kotlinx.datetime.Instant
import me.nathanfallet.ktorx.controllers.IController
import me.nathanfallet.suitebde.models.auth.*
import java.util.*

interface IAuthController : IController {

    suspend fun login(payload: LoginPayload, call: ApplicationCall)

    suspend fun register(payload: RegisterPayload, joiningAt: Instant, locale: Locale, call: ApplicationCall)
    suspend fun register(code: String, joiningAt: Instant): RegisterWithAssociationPayload
    suspend fun register(payload: RegisterCodePayload, call: ApplicationCall)

    suspend fun join(payload: JoinPayload, joiningAt: Instant, locale: Locale)
    suspend fun join(code: String, joiningAt: Instant): JoinPayload
    suspend fun join(payload: JoinCodePayload)

}
