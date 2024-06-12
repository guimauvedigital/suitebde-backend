package me.nathanfallet.suitebde.usecases.associations

import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
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
            "email", "code", "associationId", now.plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        )
        coEvery { usersRepository.getForEmail("email", false) } returns null
        coEvery { codesInEmailsRepository.createCodeInEmail("email", any(), "associationId", any()) } returns code
        val result = useCase("email", "associationId")
        assertEquals("email", result?.email)
        assertEquals("associationId", result?.associationId)
    }

    @Test
    fun invokeCodeTaken() = runBlocking {
        val codesInEmailsRepository = mockk<ICodesInEmailsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(codesInEmailsRepository, usersRepository)
        coEvery { usersRepository.getForEmail("email", false) } returns null
        coEvery { codesInEmailsRepository.createCodeInEmail("email", any(), "associationId", any()) } throws Exception()
        coEvery { codesInEmailsRepository.updateCodeInEmail("email", any(), "associationId", any()) } returns true
        val result = useCase("email", "associationId")
        assertEquals("email", result?.email)
        assertEquals("associationId", result?.associationId)
    }

    @Test
    fun invokeInternalError() = runBlocking {
        val codesInEmailsRepository = mockk<ICodesInEmailsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(codesInEmailsRepository, usersRepository)
        coEvery { usersRepository.getForEmail("email", false) } returns null
        coEvery { codesInEmailsRepository.createCodeInEmail("email", any(), "associationId", any()) } throws Exception()
        coEvery { codesInEmailsRepository.updateCodeInEmail("email", any(), "associationId", any()) } returns false
        val exception = assertFailsWith(ControllerException::class) {
            useCase("email", "associationId")
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun invokeEmailTaken() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(mockk(), usersRepository)
        coEvery { usersRepository.getForEmail("email", false) } returns User(
            "id", "associationId", "email", "password", "firstname", "lastname", false, Clock.System.now()
        )
        assertEquals(null, useCase("email", "associationId"))
    }

}
