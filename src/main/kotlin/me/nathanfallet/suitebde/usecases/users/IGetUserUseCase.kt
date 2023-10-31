package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.ISuspendUseCase

interface IGetUserUseCase : ISuspendUseCase<String, User?>