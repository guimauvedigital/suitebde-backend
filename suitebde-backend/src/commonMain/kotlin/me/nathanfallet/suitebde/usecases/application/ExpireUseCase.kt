package me.nathanfallet.suitebde.usecases.application

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.suitebde.repositories.users.IClientsInUsersRepository
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.auth.IDeleteAuthCodeUseCase

class ExpireUseCase(
    private val codesInEmailsRepository: ICodesInEmailsRepository,
    private val deleteCodeInEmailUseCase: IDeleteCodeInEmailUseCase,
    private val clientsInUsersRepository: IClientsInUsersRepository,
    private val deleteClientInUserUseCase: IDeleteAuthCodeUseCase,
) : IExpireUseCase {

    override suspend fun invoke(input: Instant) {
        codesInEmailsRepository.getCodesInEmailsExpiringBefore(input).forEach {
            deleteCodeInEmailUseCase(it.code)
        }
        clientsInUsersRepository.getExpiringBefore(input).forEach {
            deleteClientInUserUseCase(it.code)
        }
    }

}
