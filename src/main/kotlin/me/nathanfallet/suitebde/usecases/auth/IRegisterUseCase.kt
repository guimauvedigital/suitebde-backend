package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.auth.RegisterCodePayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface IRegisterUseCase : ISuspendUseCase<RegisterCodePayload, User?>