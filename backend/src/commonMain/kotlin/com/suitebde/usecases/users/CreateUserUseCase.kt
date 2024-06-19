package com.suitebde.usecases.users

import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.User
import com.suitebde.repositories.users.IUsersRepository
import com.suitebde.usecases.auth.IHashPasswordUseCase
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class CreateUserUseCase(
    private val repository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase,
) : ICreateChildModelSuspendUseCase<User, CreateUserPayload, UUID> {

    override suspend fun invoke(input1: CreateUserPayload, input2: UUID): User? {
        return repository.create(
            input1.copy(
                password = hashPasswordUseCase(input1.password)
            ),
            input2
        )
    }

}
