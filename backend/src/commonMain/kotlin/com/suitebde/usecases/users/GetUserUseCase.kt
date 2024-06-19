package com.suitebde.usecases.users

import com.suitebde.models.users.User
import com.suitebde.repositories.users.IUsersRepository
import dev.kaccelero.models.UUID

class GetUserUseCase(
    private val repository: IUsersRepository,
) : IGetUserUseCase {

    override suspend fun invoke(input: UUID): User? = repository.get(input)

}
