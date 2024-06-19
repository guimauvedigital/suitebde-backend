package com.suitebde.usecases.auth

import com.suitebde.models.auth.ClientForUser
import dev.kaccelero.usecases.ISuspendUseCase

interface IGetClientForUserForRefreshTokenUseCase : ISuspendUseCase<String, ClientForUser?>
