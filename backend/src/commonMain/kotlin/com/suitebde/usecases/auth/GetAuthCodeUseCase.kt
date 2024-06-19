package com.suitebde.usecases.auth

import com.suitebde.models.application.Client
import com.suitebde.models.auth.ClientForUser
import com.suitebde.repositories.users.IClientsInUsersRepository
import com.suitebde.usecases.users.IGetUserUseCase
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock

class GetAuthCodeUseCase(
    private val repository: IClientsInUsersRepository,
    private val getClientUseCase: IGetModelSuspendUseCase<Client, UUID>,
    private val getUserUseCase: IGetUserUseCase,
) : IGetAuthCodeUseCase {

    override suspend fun invoke(input: String): ClientForUser? {
        val clientInUser = repository.get(input)?.takeIf { it.expiration > Clock.System.now() } ?: return null
        val client = getClientUseCase(clientInUser.clientId) ?: return null
        val user = getUserUseCase(clientInUser.userId) ?: return null
        return ClientForUser(client, user)
    }

}
