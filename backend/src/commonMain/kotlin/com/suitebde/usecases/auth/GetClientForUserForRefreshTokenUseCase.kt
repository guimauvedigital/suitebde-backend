package com.suitebde.usecases.auth

import com.suitebde.models.application.Client
import com.suitebde.models.auth.ClientForUser
import com.suitebde.services.jwt.IJWTService
import com.suitebde.usecases.users.IGetUserUseCase
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.models.UUID

class GetClientForUserForRefreshTokenUseCase(
    private val jwtService: IJWTService,
    private val getUserUseCase: IGetUserUseCase,
    private val getClientUseCase: IGetModelSuspendUseCase<Client, UUID>,
) : IGetClientForUserForRefreshTokenUseCase {

    override suspend fun invoke(input: String): ClientForUser? = jwtService.verifyJWT(input)?.let {
        val user = getUserUseCase(UUID(it.subject)) ?: return@let null
        val client = it.audience.singleOrNull()?.let { clientId -> getClientUseCase(UUID(clientId)) } ?: return@let null
        ClientForUser(client, user)
    }

}
