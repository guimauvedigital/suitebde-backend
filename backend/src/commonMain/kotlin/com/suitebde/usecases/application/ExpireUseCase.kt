package com.suitebde.usecases.application

import com.suitebde.models.users.User
import com.suitebde.repositories.associations.ICodesInEmailsRepository
import com.suitebde.repositories.users.IClientsInUsersRepository
import com.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import com.suitebde.usecases.auth.IDeleteAuthCodeUseCase
import com.suitebde.usecases.users.IListUsersLastLoggedBeforeUseCase
import dev.kaccelero.commons.repositories.IDeleteChildModelSuspendUseCase
import dev.kaccelero.models.UUID
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus

class ExpireUseCase(
    private val codesInEmailsRepository: ICodesInEmailsRepository,
    private val deleteCodeInEmailUseCase: IDeleteCodeInEmailUseCase,
    private val clientsInUsersRepository: IClientsInUsersRepository,
    private val deleteClientInUserUseCase: IDeleteAuthCodeUseCase,
    private val listUsersLastLoggedBeforeUseCase: IListUsersLastLoggedBeforeUseCase,
    private val deleteUserUseCase: IDeleteChildModelSuspendUseCase<User, UUID, UUID>,
) : IExpireUseCase {

    override suspend fun invoke(input: Instant) {
        codesInEmailsRepository.getCodesInEmailsExpiringBefore(input).forEach {
            deleteCodeInEmailUseCase(it.code)
        }
        clientsInUsersRepository.getExpiringBefore(input).forEach {
            deleteClientInUserUseCase(it.code)
        }
        listUsersLastLoggedBeforeUseCase(
            input.minus(1, DateTimeUnit.YEAR, TimeZone.currentSystemDefault())
        ).forEach {
            deleteUserUseCase(it.id, it.associationId)
        }
    }

}
