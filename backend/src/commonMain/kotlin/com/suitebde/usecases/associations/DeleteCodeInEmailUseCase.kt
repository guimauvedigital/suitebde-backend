package com.suitebde.usecases.associations

import com.suitebde.repositories.associations.ICodesInEmailsRepository

class DeleteCodeInEmailUseCase(
    private val repository: ICodesInEmailsRepository,
) : IDeleteCodeInEmailUseCase {

    override suspend fun invoke(input: String) {
        repository.deleteCodeInEmail(input)
    }

}
