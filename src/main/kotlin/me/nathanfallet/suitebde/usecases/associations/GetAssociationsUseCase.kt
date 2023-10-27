package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.IAssociationsRepository

class GetAssociationsUseCase(
    private val repository: IAssociationsRepository
) : IGetAssociationsUseCase {

    override suspend fun invoke(input: Boolean): List<Association> {
        return if (input) repository.getValidatedAssociations()
        else repository.getAssociations()
    }

}