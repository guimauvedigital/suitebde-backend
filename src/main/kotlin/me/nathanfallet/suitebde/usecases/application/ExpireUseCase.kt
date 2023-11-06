package me.nathanfallet.suitebde.usecases.application

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.suitebde.usecases.associations.IDeleteAssociationUseCase
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase

class ExpireUseCase(
    private val associationsRepository: IAssociationsRepository,
    private val deleteCodeInEmailUseCase: IDeleteCodeInEmailUseCase,
    private val deleteAssociationUseCase: IDeleteAssociationUseCase
) : IExpireUseCase {

    override suspend fun invoke(input: Instant) {
        associationsRepository.getCodesInEmailsExpiringBefore(input).forEach {
            deleteCodeInEmailUseCase(it.code)
        }
        associationsRepository.getAssociationsExpiringBefore(input).forEach {
            deleteAssociationUseCase(it.id)
        }
    }

}