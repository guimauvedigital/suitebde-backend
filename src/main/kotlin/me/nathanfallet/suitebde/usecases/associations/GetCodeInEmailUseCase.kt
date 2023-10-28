package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.repositories.IAssociationsRepository

class GetCodeInEmailUseCase(
    private val repository: IAssociationsRepository
) : IGetCodeInEmailUseCase {

    override suspend fun invoke(input: Pair<String, Instant>): CodeInEmail? {
        return repository.getCodeInEmail(input.first)?.takeIf {
            it.expiresAt > input.second
        }
    }

}