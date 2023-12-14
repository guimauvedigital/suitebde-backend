package me.nathanfallet.suitebde.usecases.auth

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import me.nathanfallet.ktorx.models.auth.ClientForUser
import me.nathanfallet.ktorx.usecases.auth.ICreateAuthCodeUseCase
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IClientsInUsersRepository

class CreateAuthCodeUseCase(
    private val repository: IClientsInUsersRepository,
) : ICreateAuthCodeUseCase {

    override suspend fun invoke(input: ClientForUser): String? {
        return repository.create(
            (input.user as User).id,
            input.client.clientId,
            Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        )?.code
    }

}
