package me.nathanfallet.suitebde.usecases.application

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import me.nathanfallet.suitebde.models.auth.SessionPayload

class GetSessionForCallUseCase : IGetSessionForCallUseCase {

    override fun invoke(input: ApplicationCall): SessionPayload? {
        return input.sessions.get<SessionPayload>()
    }

}