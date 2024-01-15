package me.nathanfallet.suitebde.usecases.application

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import me.nathanfallet.usecases.models.delete.IDeleteModelSuspendUseCase

class ExpireUseCase(
    private val codesInEmailsRepository: ICodesInEmailsRepository,
    private val deleteCodeInEmailUseCase: IDeleteCodeInEmailUseCase,
    private val associationsRepository: IAssociationsRepository,
    private val deleteAssociationUseCase: IDeleteModelSuspendUseCase<Association, String>,
) : IExpireUseCase {

    override suspend fun invoke(input: Instant) {
        codesInEmailsRepository.getCodesInEmailsExpiringBefore(input).forEach {
            deleteCodeInEmailUseCase(it.code)
        }
        associationsRepository.getAssociationsExpiringBefore(input).forEach {
            deleteAssociationUseCase(it.id)
        }
    }

}
