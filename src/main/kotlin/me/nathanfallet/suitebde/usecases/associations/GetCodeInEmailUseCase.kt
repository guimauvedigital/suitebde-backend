package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.repositories.IAssociationsRepository

class GetCodeInEmailUseCase(
    private val repository: IAssociationsRepository
) : IGetCodeInEmailUseCase {

    override suspend fun invoke(input: String): CodeInEmail? {
        return repository.getCodeInEmail(input)?.takeIf {
            it.expiresAt > Clock.System.now()
        }
    }

}