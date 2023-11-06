package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.ISuspendUseCase

interface ICreateUserUseCase : ISuspendUseCase<CreateUserPayload, User?>