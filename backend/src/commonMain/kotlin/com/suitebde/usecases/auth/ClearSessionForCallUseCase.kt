package com.suitebde.usecases.auth

import com.suitebde.models.auth.SessionPayload
import io.ktor.server.application.*
import io.ktor.server.sessions.*

class ClearSessionForCallUseCase : IClearSessionForCallUseCase {

    override fun invoke(input: ApplicationCall) = input.sessions.clear<SessionPayload>()

}
