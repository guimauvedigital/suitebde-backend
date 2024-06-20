package com.suitebde.usecases.auth

import com.suitebde.models.auth.LoginPayload
import com.suitebde.models.users.User
import dev.kaccelero.usecases.ISuspendUseCase

interface ILoginUseCase : ISuspendUseCase<LoginPayload, User?>
