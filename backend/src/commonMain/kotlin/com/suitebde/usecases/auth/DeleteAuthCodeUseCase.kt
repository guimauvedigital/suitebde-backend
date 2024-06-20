package com.suitebde.usecases.auth

import com.suitebde.repositories.users.IClientsInUsersRepository

class DeleteAuthCodeUseCase(
    private val repository: IClientsInUsersRepository,
) : IDeleteAuthCodeUseCase {

    override suspend fun invoke(input: String): Boolean = repository.delete(input)

}
