package com.suitebde.usecases.auth

import com.suitebde.models.auth.AuthToken
import com.suitebde.models.auth.ClientForUser
import dev.kaccelero.usecases.ISuspendUseCase

interface IGenerateAuthTokenUseCase : ISuspendUseCase<ClientForUser, AuthToken>
