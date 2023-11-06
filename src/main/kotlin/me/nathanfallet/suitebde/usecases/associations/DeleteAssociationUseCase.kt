package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository

class DeleteAssociationUseCase(
    private val repository: IAssociationsRepository
) : IDeleteAssociationUseCase {

    override suspend fun invoke(input: String) {
        repository.delete(input)
    }

}