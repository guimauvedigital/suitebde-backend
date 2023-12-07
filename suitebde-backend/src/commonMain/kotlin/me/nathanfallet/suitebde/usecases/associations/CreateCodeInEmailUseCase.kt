package me.nathanfallet.suitebde.usecases.associations

import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.suitebde.repositories.users.IUsersRepository

class CreateCodeInEmailUseCase(
    private val codesInEmailsRepository: ICodesInEmailsRepository,
    private val usersRepository: IUsersRepository,
) : ICreateCodeInEmailUseCase {

    override suspend fun invoke(input1: String, input2: String?): CodeInEmail? {
        usersRepository.getForEmail(input1, false)?.let {
            return null
        }
        val code = String.generateId()
        val expiresAt = Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        return try {
            codesInEmailsRepository.createCodeInEmail(input1, code, input2, expiresAt)
        } catch (e: Exception) {
            codesInEmailsRepository.updateCodeInEmail(input1, code, input2, expiresAt).takeIf {
                it == 1
            } ?: throw ControllerException(HttpStatusCode.InternalServerError, "error_internal")
            CodeInEmail(input1, code, input2, expiresAt)
        }
    }

}
