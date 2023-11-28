package me.nathanfallet.suitebde.usecases.application

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.auth.SessionPayload
import me.nathanfallet.usecases.base.IUseCase

interface IGetSessionForCallUseCase : IUseCase<ApplicationCall, SessionPayload?>