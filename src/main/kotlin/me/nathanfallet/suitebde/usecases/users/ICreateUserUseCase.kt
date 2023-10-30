package me.nathanfallet.suitebde.usecases.users

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface ICreateUserUseCase : ISuspendUseCase<Pair<CreateUserPayload, Instant>, User?>