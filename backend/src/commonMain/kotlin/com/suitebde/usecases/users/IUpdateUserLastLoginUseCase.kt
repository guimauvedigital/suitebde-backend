package com.suitebde.usecases.users

import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.ISuspendUseCase

interface IUpdateUserLastLoginUseCase : ISuspendUseCase<UUID, Boolean>
