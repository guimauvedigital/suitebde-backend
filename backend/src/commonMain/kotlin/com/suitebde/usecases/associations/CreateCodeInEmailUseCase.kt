package com.suitebde.usecases.associations

import com.suitebde.extensions.generateId
import com.suitebde.models.associations.CodeInEmail
import com.suitebde.repositories.associations.ICodesInEmailsRepository
import com.suitebde.repositories.users.IUsersRepository
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.models.UUID
import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

class CreateCodeInEmailUseCase(
    private val codesInEmailsRepository: ICodesInEmailsRepository,
    private val usersRepository: IUsersRepository,
) : ICreateCodeInEmailUseCase {

    override suspend fun invoke(input1: String, input2: UUID?): CodeInEmail? {
        usersRepository.getForEmail(input1, false)?.let {
            return null
        }
        val code = String.generateId()
        val expiresAt = Clock.System.now().plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        return try {
            codesInEmailsRepository.createCodeInEmail(input1, code, input2, expiresAt)
        } catch (e: Exception) {
            codesInEmailsRepository.updateCodeInEmail(input1, code, input2, expiresAt).takeIf {
                it
            } ?: throw ControllerException(HttpStatusCode.InternalServerError, "error_internal")
            CodeInEmail(input1, code, input2, expiresAt)
        }
    }

}
