package com.suitebde.usecases.users

import com.suitebde.repositories.users.IUsersRepository
import dev.kaccelero.models.UUID

class UpdateUserLastLoginUseCase(
    private val repository: IUsersRepository,
) : IUpdateUserLastLoginUseCase {

    override suspend fun invoke(input: UUID): Boolean = repository.updateLastLogin(input)

}
