package me.nathanfallet.suitebde.usecases.users

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.ISuspendUseCase

interface IGetUserForCallUseCase : ISuspendUseCase<ApplicationCall, User?>