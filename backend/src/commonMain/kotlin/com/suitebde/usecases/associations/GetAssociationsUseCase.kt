package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import com.suitebde.repositories.associations.IAssociationsRepository

class GetAssociationsUseCase(
    private val repository: IAssociationsRepository,
) : IGetAssociationsUseCase {

    override suspend fun invoke(input: Boolean): List<Association> {
        return if (input) repository.getValidatedAssociations()
        else repository.list()
    }

}
