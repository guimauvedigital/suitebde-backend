package me.nathanfallet.suitebde.repositories.auth

import me.nathanfallet.suitebde.models.auth.RefreshTokenPayload
import me.nathanfallet.usecases.auth.AuthRequest
import me.nathanfallet.usecases.auth.AuthToken

interface IAuthAPIRemoteRepository {

    suspend fun token(payload: AuthRequest): AuthToken?
    suspend fun refresh(payload: RefreshTokenPayload): AuthToken?

}
