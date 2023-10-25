package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.IAssociationsRepository

class GetAssociationForDomainUseCase(
    private val repository: IAssociationsRepository
) : IGetAssociationForDomainUseCase {

    override suspend fun invoke(input: String): Association? {
        return repository.getAssociationForDomain(input)
    }

}