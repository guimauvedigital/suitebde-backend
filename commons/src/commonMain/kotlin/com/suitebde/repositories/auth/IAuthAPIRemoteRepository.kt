package com.suitebde.repositories.auth

import com.suitebde.models.auth.AuthRequest
import com.suitebde.models.auth.AuthToken
import com.suitebde.models.auth.RefreshTokenPayload

interface IAuthAPIRemoteRepository {

    suspend fun token(payload: AuthRequest): AuthToken?
    suspend fun refresh(payload: RefreshTokenPayload): AuthToken?

}
