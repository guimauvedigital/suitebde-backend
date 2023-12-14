package me.nathanfallet.suitebde.usecases.auth

import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.auth.ClientForUser
import me.nathanfallet.ktorx.usecases.auth.IGetAuthCodeUseCase
import me.nathanfallet.ktorx.usecases.auth.IGetClientUseCase
import me.nathanfallet.suitebde.repositories.users.IClientsInUsersRepository
import me.nathanfallet.suitebde.usecases.users.IGetUserUseCase

class GetAuthCodeUseCase(
    private val repository: IClientsInUsersRepository,
    private val getClientUseCase: IGetClientUseCase,
    private val getUserUseCase: IGetUserUseCase,
) : IGetAuthCodeUseCase {

    override suspend fun invoke(input: String): ClientForUser? {
        val clientInUser = repository.get(input)?.takeIf { it.expiration > Clock.System.now() } ?: return null
        val client = getClientUseCase(clientInUser.clientId) ?: return null
        val user = getUserUseCase(clientInUser.userId) ?: return null
        return ClientForUser(client, user)
    }

}
