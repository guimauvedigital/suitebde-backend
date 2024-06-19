package com.suitebde.usecases.users

import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.ISuspendUseCase

interface IGetUserUseCase : ISuspendUseCase<UUID, User?>
