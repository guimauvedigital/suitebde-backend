package com.suitebde.usecases.auth

import com.suitebde.repositories.users.IResetsInUsersRepository

class DeleteResetPasswordUseCase(
    private val repository: IResetsInUsersRepository,
) : IDeleteResetPasswordUseCase {

    override suspend fun invoke(input: String) = repository.delete(input)

}
