package com.suitebde.usecases.auth

import com.suitebde.models.auth.SessionPayload
import io.ktor.server.application.*
import io.ktor.server.sessions.*

class GetSessionForCallUseCase : IGetSessionForCallUseCase {

    override fun invoke(input: ApplicationCall): SessionPayload? = input.sessions.get()

}
