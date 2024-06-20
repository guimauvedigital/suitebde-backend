package com.suitebde.usecases.auth

import com.suitebde.models.auth.RegisterCodePayload
import com.suitebde.models.users.User
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IRegisterUseCase : IPairSuspendUseCase<String, RegisterCodePayload, User?>
