package com.suitebde.usecases.auth

import com.suitebde.models.users.ResetInUser
import dev.kaccelero.usecases.ISuspendUseCase

interface ICreateResetPasswordUseCase : ISuspendUseCase<String, ResetInUser?>
