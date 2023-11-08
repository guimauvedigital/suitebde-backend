package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IUpdateUserUseCase : IPairSuspendUseCase<String, UpdateUserPayload, User?>