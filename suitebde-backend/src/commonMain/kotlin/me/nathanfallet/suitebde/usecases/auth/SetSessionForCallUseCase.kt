package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import me.nathanfallet.suitebde.models.auth.SessionPayload

class SetSessionForCallUseCase : ISetSessionForCallUseCase {

    override fun invoke(input1: ApplicationCall, input2: SessionPayload) = input1.sessions.set(input2)

}
