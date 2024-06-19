package com.suitebde.usecases.auth

import com.suitebde.models.auth.AuthToken
import com.suitebde.models.auth.ClientForUser
import com.suitebde.services.jwt.IJWTService

class GenerateAuthTokenUseCase(
    private val jwtService: IJWTService,
) : IGenerateAuthTokenUseCase {

    override suspend fun invoke(input: ClientForUser) = AuthToken(
        jwtService.generateJWT(input.user.id, input.client.id, "access"),
        jwtService.generateJWT(input.user.id, input.client.id, "refresh"),
        "${input.user.associationId}/${input.user.id}"
    )

}
