package com.suitebde.usecases.associations

import com.suitebde.models.associations.CodeInEmail
import com.suitebde.repositories.associations.ICodesInEmailsRepository
import kotlinx.datetime.Clock

class GetCodeInEmailUseCase(
    private val repository: ICodesInEmailsRepository,
) : IGetCodeInEmailUseCase {

    override suspend fun invoke(input: String): CodeInEmail? =
        repository.getCodeInEmail(input)?.takeIf {
            it.expiresAt > Clock.System.now()
        }

}
