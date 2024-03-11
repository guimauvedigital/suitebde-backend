package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.auth.ClientForUser
import me.nathanfallet.usecases.auth.AuthToken
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IGenerateAuthTokenUseCase : ISuspendUseCase<ClientForUser, AuthToken>
