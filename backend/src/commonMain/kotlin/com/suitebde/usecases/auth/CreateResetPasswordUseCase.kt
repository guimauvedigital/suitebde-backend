package com.suitebde.usecases.auth

import com.suitebde.models.users.ResetInUser
import com.suitebde.repositories.users.IResetsInUsersRepository
import com.suitebde.usecases.users.IGetUserForEmailUseCase
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

class CreateResetPasswordUseCase(
    private val getUserForEmailUseCase: IGetUserForEmailUseCase,
    private val repository: IResetsInUsersRepository,
) : ICreateResetPasswordUseCase {

    override suspend fun invoke(input: String): ResetInUser? {
        val user = getUserForEmailUseCase(input, false) ?: return null
        return repository.create(
            user.id,
            Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        )
    }

}
