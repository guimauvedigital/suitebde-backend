package com.suitebde.usecases.auth

import com.suitebde.models.auth.SessionPayload
import io.ktor.server.application.*
import io.ktor.server.sessions.*

class SetSessionForCallUseCase : ISetSessionForCallUseCase {

    override fun invoke(input1: ApplicationCall, input2: SessionPayload) = input1.sessions.set(input2)

}
