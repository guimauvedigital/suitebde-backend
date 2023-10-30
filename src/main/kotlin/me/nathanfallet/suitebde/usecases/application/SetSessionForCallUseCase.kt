package me.nathanfallet.suitebde.usecases.application

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import me.nathanfallet.suitebde.models.auth.SessionPayload

class SetSessionForCallUseCase : ISetSessionForCallUseCase {

    override fun invoke(input: Pair<ApplicationCall, SessionPayload>) {
        input.first.sessions.set(input.second)
    }

}