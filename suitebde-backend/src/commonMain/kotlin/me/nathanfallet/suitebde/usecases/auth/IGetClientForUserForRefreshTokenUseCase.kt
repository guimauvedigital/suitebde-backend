package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.auth.ClientForUser
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IGetClientForUserForRefreshTokenUseCase : ISuspendUseCase<String, ClientForUser?>
