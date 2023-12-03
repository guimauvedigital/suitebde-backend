package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import me.nathanfallet.ktorx.usecases.auth.IGetSessionForCallUseCase
import me.nathanfallet.suitebde.models.auth.SessionPayload
import me.nathanfallet.usecases.users.ISessionPayload

class GetSessionForCallUseCase : IGetSessionForCallUseCase {

    override fun invoke(input: ApplicationCall): ISessionPayload? {
        return input.sessions.get<SessionPayload>()
    }

}
