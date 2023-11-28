package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.usecases.models.delete.IDeleteModelSuspendUseCase

class DeleteAssociationUseCase(
    private val repository: IAssociationsRepository
) : IDeleteModelSuspendUseCase<Association, String> {

    override suspend fun invoke(input: String): Boolean {
        return repository.delete(input)
    }

}