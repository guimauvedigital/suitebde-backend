package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository

class GetCodeInEmailUseCase(
    private val repository: IAssociationsRepository
) : IGetCodeInEmailUseCase {

    override suspend fun invoke(input1: String, input2: Instant): CodeInEmail? {
        return repository.getCodeInEmail(input1)?.takeIf {
            it.expiresAt > input2
        }
    }

}