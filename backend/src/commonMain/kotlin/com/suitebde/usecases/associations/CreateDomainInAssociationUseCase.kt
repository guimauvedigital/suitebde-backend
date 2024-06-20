package com.suitebde.usecases.associations

import com.suitebde.models.associations.CreateDomainInAssociationPayload
import com.suitebde.models.associations.DomainInAssociation
import com.suitebde.repositories.associations.IDomainsInAssociationsRepository
import com.suitebde.usecases.application.ISetupDomainUseCase
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class CreateDomainInAssociationUseCase(
    private val repository: IDomainsInAssociationsRepository,
    private val setupDomainUseCase: ISetupDomainUseCase,
) : ICreateChildModelSuspendUseCase<DomainInAssociation, CreateDomainInAssociationPayload, UUID> {

    override suspend fun invoke(input1: CreateDomainInAssociationPayload, input2: UUID): DomainInAssociation? {
        // Check if domain is not already in use
        if (repository.get(input1.domain, input2) != null) return null
        if (!setupDomainUseCase(input1.domain)) return null
        return repository.create(input1, input2)
    }

}
