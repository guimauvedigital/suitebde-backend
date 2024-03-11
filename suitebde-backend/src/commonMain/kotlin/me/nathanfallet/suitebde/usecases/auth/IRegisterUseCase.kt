package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.auth.RegisterCodePayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IRegisterUseCase : IPairSuspendUseCase<ApplicationCall, RegisterCodePayload, User?>
