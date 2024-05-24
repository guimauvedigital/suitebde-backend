package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.application.Client
import me.nathanfallet.suitebde.models.auth.ClientForUser
import me.nathanfallet.suitebde.services.jwt.IJWTService
import me.nathanfallet.suitebde.usecases.users.IGetUserUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase

class GetClientForUserForRefreshTokenUseCase(
    private val jwtService: IJWTService,
    private val getUserUseCase: IGetUserUseCase,
    private val getClientUseCase: IGetModelSuspendUseCase<Client, String>,
) : IGetClientForUserForRefreshTokenUseCase {

    override suspend fun invoke(input: String): ClientForUser? = jwtService.verifyJWT(input)?.let {
        val user = getUserUseCase(it.subject) ?: return@let null
        val client = it.audience.singleOrNull()?.let { clientId -> getClientUseCase(clientId) } ?: return@let null
        ClientForUser(client, user)
    }

}
