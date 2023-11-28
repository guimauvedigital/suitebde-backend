package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import me.nathanfallet.suitebde.usecases.application.IShutdownDomainUseCase
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase

class DeleteDomainInAssociationUseCase(
    private val repository: IDomainsInAssociationsRepository,
    private val shutdownDomainUseCase: IShutdownDomainUseCase
) : IDeleteChildModelSuspendUseCase<DomainInAssociation, String, String> {

    override suspend fun invoke(input1: String, input2: String): Boolean {
        return repository.delete(input1, input2) && shutdownDomainUseCase(input1)
    }

}
