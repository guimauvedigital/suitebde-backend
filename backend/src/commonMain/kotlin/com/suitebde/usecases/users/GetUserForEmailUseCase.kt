package com.suitebde.usecases.users

import com.suitebde.models.users.User
import com.suitebde.repositories.users.IUsersRepository

class GetUserForEmailUseCase(
    private val repository: IUsersRepository,
) : IGetUserForEmailUseCase {

    override suspend fun invoke(input1: String, input2: Boolean): User? =
        repository.getForEmail(input1, input2)

}
