package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import me.nathanfallet.ktorx.usecases.auth.ISetSessionForCallUseCase
import me.nathanfallet.suitebde.models.auth.SessionPayload
import me.nathanfallet.usecases.users.ISessionPayload

class SetSessionForCallUseCase : ISetSessionForCallUseCase {

    override fun invoke(input1: ApplicationCall, input2: ISessionPayload) {
        if (input2 !is SessionPayload) return
        input1.sessions.set(input2)
    }

}
