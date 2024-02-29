package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.IPairSuspendUseCase

interface IGetUserForEmailUseCase : IPairSuspendUseCase<String, Boolean, User?>
