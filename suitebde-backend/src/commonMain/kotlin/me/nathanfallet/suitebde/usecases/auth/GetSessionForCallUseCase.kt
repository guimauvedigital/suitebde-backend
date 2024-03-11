package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import me.nathanfallet.suitebde.models.auth.SessionPayload

class GetSessionForCallUseCase : IGetSessionForCallUseCase {

    override fun invoke(input: ApplicationCall): SessionPayload? = input.sessions.get()

}
