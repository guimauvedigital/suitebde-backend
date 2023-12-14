package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.ktorx.usecases.auth.IDeleteAuthCodeUseCase
import me.nathanfallet.suitebde.repositories.users.IClientsInUsersRepository

class DeleteAuthCodeUseCase(
    private val repository: IClientsInUsersRepository,
) : IDeleteAuthCodeUseCase {

    override suspend fun invoke(input: String): Boolean {
        return repository.delete(input)
    }

}
