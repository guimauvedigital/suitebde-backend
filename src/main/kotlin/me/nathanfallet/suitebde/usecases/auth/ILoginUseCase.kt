package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface ILoginUseCase : ISuspendUseCase<LoginPayload, User?>