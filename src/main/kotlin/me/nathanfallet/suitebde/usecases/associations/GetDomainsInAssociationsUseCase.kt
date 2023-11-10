package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.repositories.associations.IDomainsInAssociationsRepository

class GetDomainsInAssociationsUseCase(
    private val domainsInAssociationsRepository: IDomainsInAssociationsRepository
) : IGetDomainsInAssociationsUseCase {

    override suspend fun invoke(input: String): List<DomainInAssociation> {
        return domainsInAssociationsRepository.getDomains(input)
    }

}
