package com.suitebde.usecases.users

import com.suitebde.models.users.User
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IGetUserForEmailUseCase : IPairSuspendUseCase<String, Boolean, User?>
