package me.nathanfallet.suitebde.usecases.auth

import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.application.Client
import me.nathanfallet.suitebde.models.auth.ClientForUser
import me.nathanfallet.suitebde.repositories.users.IClientsInUsersRepository
import me.nathanfallet.suitebde.usecases.users.IGetUserUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase

class GetAuthCodeUseCase(
    private val repository: IClientsInUsersRepository,
    private val getClientUseCase: IGetModelSuspendUseCase<Client, String>,
    private val getUserUseCase: IGetUserUseCase,
) : IGetAuthCodeUseCase {

    override suspend fun invoke(input: String): ClientForUser? {
        val clientInUser = repository.get(input)?.takeIf { it.expiration > Clock.System.now() } ?: return null
        val client = getClientUseCase(clientInUser.clientId) ?: return null
        val user = getUserUseCase(clientInUser.userId) ?: return null
        return ClientForUser(client, user)
    }

}
