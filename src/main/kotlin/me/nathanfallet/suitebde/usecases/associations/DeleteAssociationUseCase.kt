package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.IAssociationsRepository

class DeleteAssociationUseCase(
    private val repository: IAssociationsRepository
) : IDeleteAssociationUseCase {

    override suspend fun invoke(input: Association) {
        repository.deleteAssociation(input)
    }

}