package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository

class DeleteCodeInEmailUseCase(
    private val repository: IAssociationsRepository
) : IDeleteCodeInEmailUseCase {

    override suspend fun invoke(input: String) {
        repository.deleteCodeInEmail(input)
    }

}