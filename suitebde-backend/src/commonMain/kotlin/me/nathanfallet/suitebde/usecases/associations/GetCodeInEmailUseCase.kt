package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository

class GetCodeInEmailUseCase(
    private val repository: ICodesInEmailsRepository,
) : IGetCodeInEmailUseCase {

    override suspend fun invoke(input: String): CodeInEmail? {
        return repository.getCodeInEmail(input)?.takeIf {
            it.expiresAt > Clock.System.now()
        }
    }

}
