package com.suitebde.usecases.auth

import com.suitebde.models.auth.SessionPayload
import dev.kaccelero.usecases.IUseCase
import io.ktor.server.application.*

interface IGetSessionForCallUseCase : IUseCase<ApplicationCall, SessionPayload?>
