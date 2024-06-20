package com.suitebde.usecases.associations

import com.suitebde.models.associations.DomainInAssociation
import com.suitebde.repositories.associations.IDomainsInAssociationsRepository
import com.suitebde.usecases.application.IShutdownDomainUseCase
import dev.kaccelero.commons.repositories.IDeleteChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class DeleteDomainInAssociationUseCase(
    private val repository: IDomainsInAssociationsRepository,
    private val shutdownDomainUseCase: IShutdownDomainUseCase,
) : IDeleteChildModelSuspendUseCase<DomainInAssociation, String, UUID> {

    override suspend fun invoke(input1: String, input2: UUID): Boolean =
        repository.delete(input1, input2) && shutdownDomainUseCase(input1)

}
