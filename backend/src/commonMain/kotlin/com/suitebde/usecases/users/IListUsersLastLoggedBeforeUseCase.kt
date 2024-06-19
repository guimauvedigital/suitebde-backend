package com.suitebde.usecases.users

import com.suitebde.models.users.User
import dev.kaccelero.usecases.ISuspendUseCase
import kotlinx.datetime.Instant

interface IListUsersLastLoggedBeforeUseCase : ISuspendUseCase<Instant, List<User>>
