package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.IAssociationsRepository

class UpdateAssociationUseCase(
    private val repository: IAssociationsRepository
) : IUpdateAssociationUseCase {

    override suspend fun invoke(input: Association): Association? {
        return if (repository.updateAssociation(input) == 1) input
        else null
    }

}