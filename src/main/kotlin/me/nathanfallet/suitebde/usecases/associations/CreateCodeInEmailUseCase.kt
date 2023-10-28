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
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.utils.IdHelper

class CreateCodeInEmailUseCase(
    private val associationsRepository: IAssociationsRepository,
    private val usersRepository: IUsersRepository
) : ICreateCodeInEmailUseCase {

    override suspend fun invoke(input: Pair<String, String?>): CodeInEmail? {
        usersRepository.getUserForEmail(input.first, false)?.let {
            return null
        }
        val code = IdHelper.generateId().take(32)
        val expiresAt = Clock.System.now().plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
        return try {
            associationsRepository.createCodeInEmail(input.first, code, input.second, expiresAt)
        } catch (e: Exception) {
            associationsRepository.updateCodeInEmail(input.first, code, input.second, expiresAt).takeIf {
                it == 1
            } ?: throw ControllerException(HttpStatusCode.InternalServerError, LocalizedString.ERROR_INTERNAL)
            CodeInEmail(input.first, code, input.second, expiresAt)
        }
    }

}