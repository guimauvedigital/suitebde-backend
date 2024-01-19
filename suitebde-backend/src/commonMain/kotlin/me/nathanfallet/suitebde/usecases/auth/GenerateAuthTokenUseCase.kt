package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.ktorx.models.auth.ClientForUser
import me.nathanfallet.ktorx.usecases.auth.IGenerateAuthTokenUseCase
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.services.jwt.IJWTService
import me.nathanfallet.usecases.auth.AuthToken

class GenerateAuthTokenUseCase(
    private val jwtService: IJWTService,
) : IGenerateAuthTokenUseCase {

    override suspend fun invoke(input: ClientForUser): AuthToken {
        val user = input.user as User
        return AuthToken(
            jwtService.generateJWT(user.id, input.client.clientId, "access"),
            jwtService.generateJWT(user.id, input.client.clientId, "refresh"),
            "${user.associationId}/${user.id}"
        )
    }

}
