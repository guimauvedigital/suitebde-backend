package com.suitebde.usecases.auth

import com.suitebde.models.users.ResetInUser
import dev.kaccelero.usecases.ISuspendUseCase

interface IGetResetPasswordUseCase : ISuspendUseCase<String, ResetInUser?>
