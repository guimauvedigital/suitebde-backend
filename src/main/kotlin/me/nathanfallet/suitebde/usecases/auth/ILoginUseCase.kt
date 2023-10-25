package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.ISuspendUseCase

interface ILoginUseCase : ISuspendUseCase<Triple<String, String, String>, User>