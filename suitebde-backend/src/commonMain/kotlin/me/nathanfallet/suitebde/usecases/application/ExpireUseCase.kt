package me.nathanfallet.suitebde.usecases.application

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.suitebde.repositories.users.IClientsInUsersRepository
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.auth.IDeleteAuthCodeUseCase
import me.nathanfallet.suitebde.usecases.users.IListUsersLastLoggedBeforeUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase

class ExpireUseCase(
    private val codesInEmailsRepository: ICodesInEmailsRepository,
    private val deleteCodeInEmailUseCase: IDeleteCodeInEmailUseCase,
    private val clientsInUsersRepository: IClientsInUsersRepository,
    private val deleteClientInUserUseCase: IDeleteAuthCodeUseCase,
    private val listUsersLastLoggedBeforeUseCase: IListUsersLastLoggedBeforeUseCase,
    private val deleteUserUseCase: IDeleteChildModelSuspendUseCase<User, String, String>,
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
