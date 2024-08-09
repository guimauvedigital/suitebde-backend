package com.suitebde.usecases.auth

import com.suitebde.models.users.ResetInUser
import com.suitebde.repositories.users.IResetsInUsersRepository

class GetResetPasswordUseCase(
    private val repository: IResetsInUsersRepository,
) : IGetResetPasswordUseCase {

    override suspend fun invoke(input: String): ResetInUser? = repository.get(input)

}
