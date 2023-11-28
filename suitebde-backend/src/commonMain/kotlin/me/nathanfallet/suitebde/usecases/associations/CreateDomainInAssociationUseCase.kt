package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.CreateDomainInAssociationPayload
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository
import me.nathanfallet.suitebde.usecases.application.ISetupDomainUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase

class CreateDomainInAssociationUseCase(
    private val repository: IDomainsInAssociationsRepository,
    private val setupDomainUseCase: ISetupDomainUseCase
) : ICreateChildModelSuspendUseCase<DomainInAssociation, CreateDomainInAssociationPayload, String> {

    override suspend fun invoke(input1: CreateDomainInAssociationPayload, input2: String): DomainInAssociation? {
        // Check if domain is not already in use
        if (repository.get(input1.domain, input2) != null) return null
        if (!setupDomainUseCase(input1.domain)) return null
        return repository.create(input1, input2)
    }

}
