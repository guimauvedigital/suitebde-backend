package me.nathanfallet.suitebde.usecases.associations

import io.ktor.http.*
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import me.nathanfallet.suitebde.extensions.generateId
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.IUsersRepository

class CreateCodeInEmailUseCase(
    private val associationsRepository: IAssociationsRepository,
    private val usersRepository: IUsersRepository
) : ICreateCodeInEmailUseCase {

    override suspend fun invoke(input1: String, input2: String?, input3: Instant): CodeInEmail? {
        usersRepository.getUserForEmail(input1, false)?.let {
            return null
        }
        val code = String.generateId()
        val expiresAt = input3.plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        return try {
            associationsRepository.createCodeInEmail(input1, code, input2, expiresAt)
        } catch (e: Exception) {
            associationsRepository.updateCodeInEmail(input1, code, input2, expiresAt).takeIf {
                it == 1
            } ?: throw ControllerException(HttpStatusCode.InternalServerError, "error_internal")
            CodeInEmail(input1, code, input2, expiresAt)
        }
    }

}