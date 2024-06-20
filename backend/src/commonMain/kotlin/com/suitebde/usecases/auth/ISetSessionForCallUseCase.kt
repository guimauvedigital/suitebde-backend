package com.suitebde.usecases.auth

import com.suitebde.models.auth.SessionPayload
import dev.kaccelero.usecases.IPairUseCase
import io.ktor.server.application.*

interface ISetSessionForCallUseCase : IPairUseCase<ApplicationCall, SessionPayload, Unit>
