package me.nathanfallet.suitebde.usecases.associations

import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.utils.IdHelper

class CreateCodeInEmailUseCase(
    private val repository: IAssociationsRepository
) : ICreateCodeInEmailUseCase {

    override suspend fun invoke(input: Pair<String, String?>): CodeInEmail? {
        // TODO: Check email availability + still valid code (not expired)
        val code = IdHelper.generateId().take(32)
        val expiresAt = Clock.System.now().plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
        return try {
            repository.createCodeInEmail(input.first, code, input.second, expiresAt)
        } catch (e: Exception) {
            repository.updateCodeInEmail(input.first, code, input.second, expiresAt).takeIf {
                it == 1
            } ?: throw ControllerException(HttpStatusCode.InternalServerError, LocalizedString.ERROR_INTERNAL)
            CodeInEmail(input.first, code, input.second, expiresAt)
        }
    }

}