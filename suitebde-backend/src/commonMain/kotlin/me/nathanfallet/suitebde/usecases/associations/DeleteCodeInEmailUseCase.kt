package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository

class DeleteCodeInEmailUseCase(
    private val repository: ICodesInEmailsRepository,
) : IDeleteCodeInEmailUseCase {

    override suspend fun invoke(input: String) {
        repository.deleteCodeInEmail(input)
    }

}
