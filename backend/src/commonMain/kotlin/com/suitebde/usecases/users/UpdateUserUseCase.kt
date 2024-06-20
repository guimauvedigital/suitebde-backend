package com.suitebde.usecases.users

import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import com.suitebde.repositories.users.IUsersRepository
import com.suitebde.usecases.auth.IHashPasswordUseCase
import dev.kaccelero.commons.repositories.IUpdateChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class UpdateUserUseCase(
    private val repository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase,
    private val getUserUseCase: IGetUserUseCase,
) : IUpdateChildModelSuspendUseCase<User, UUID, UpdateUserPayload, UUID> {

    override suspend fun invoke(input1: UUID, input2: UpdateUserPayload, input3: UUID): User? {
        val updatedUser = input2.password?.let {
            input2.copy(password = hashPasswordUseCase(it))
        } ?: input2
        return if (repository.update(input1, updatedUser, input3)) getUserUseCase(input1)
        else null
    }

}
