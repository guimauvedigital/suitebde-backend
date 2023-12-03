package me.nathanfallet.suitebde.controllers.auth

import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.auth.IAuthWithCodeController
import me.nathanfallet.suitebde.models.auth.*

interface IAuthController : IAuthWithCodeController<LoginPayload, RegisterPayload, RegisterCodePayload> {

    suspend fun join(call: ApplicationCall, payload: JoinPayload)
    suspend fun join(call: ApplicationCall, code: String): JoinPayload
    suspend fun join(call: ApplicationCall, code: String, payload: JoinCodePayload)

}
