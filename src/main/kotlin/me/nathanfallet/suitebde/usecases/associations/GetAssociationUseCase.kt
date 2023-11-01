package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.IAssociationsRepository

class GetAssociationUseCase(
    private val repository: IAssociationsRepository
) : IGetAssociationUseCase {

    override suspend fun invoke(input: String): Association? {
        return repository.getAssociation(input)
    }

}