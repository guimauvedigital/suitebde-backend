package com.suitebde.usecases.associations

import com.suitebde.models.associations.CodeInEmail
import com.suitebde.models.users.User
import com.suitebde.repositories.associations.ICodesInEmailsRepository
import com.suitebde.repositories.users.IUsersRepository
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateCodeInEmailUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val codesInEmailsRepository = mockk<ICodesInEmailsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(codesInEmailsRepository, usersRepository)
        val now = Clock.System.now()
        val code = CodeInEmail(
            "email", "code", UUID(), now.plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        )
        coEvery { usersRepository.getForEmail("email", false) } returns null
        coEvery { codesInEmailsRepository.createCodeInEmail("email", any(), UUID(), any()) } returns code
        val result = useCase("email", UUID())
        assertEquals("email", result?.email)
        assertEquals(UUID(), result?.associationId)
    }

    @Test
    fun invokeCodeTaken() = runBlocking {
        val codesInEmailsRepository = mockk<ICodesInEmailsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(codesInEmailsRepository, usersRepository)
        coEvery { usersRepository.getForEmail("email", false) } returns null
        coEvery { codesInEmailsRepository.createCodeInEmail("email", any(), UUID(), any()) } throws Exception()
        coEvery { codesInEmailsRepository.updateCodeInEmail("email", any(), UUID(), any()) } returns true
        val result = useCase("email", UUID())
        assertEquals("email", result?.email)
        assertEquals(UUID(), result?.associationId)
    }

    @Test
    fun invokeInternalError() = runBlocking {
        val codesInEmailsRepository = mockk<ICodesInEmailsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(codesInEmailsRepository, usersRepository)
        coEvery { usersRepository.getForEmail("email", false) } returns null
        coEvery { codesInEmailsRepository.createCodeInEmail("email", any(), UUID(), any()) } throws Exception()
        coEvery { codesInEmailsRepository.updateCodeInEmail("email", any(), UUID(), any()) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            useCase("email", UUID())
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun invokeEmailTaken() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(mockk(), usersRepository)
        coEvery { usersRepository.getForEmail("email", false) } returns User(
            UUID(), UUID(), "email", "password", "firstname", "lastname", false, Clock.System.now()
        )
        assertEquals(null, useCase("email", UUID()))
    }

}
