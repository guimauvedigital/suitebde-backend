package me.nathanfallet.suitebde.usecases.users

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IListUsersLastLoggedBeforeUseCase : ISuspendUseCase<Instant, List<User>>
