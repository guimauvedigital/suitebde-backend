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
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateCodeInEmailUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(associationsRepository, usersRepository)
        val now = Clock.System.now()
        val code = CodeInEmail(
            "email", "code", "associationId", now.plus(1, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        )
        coEvery { usersRepository.getForEmail("email", false) } returns null
        coEvery { associationsRepository.createCodeInEmail("email", any(), "associationId", any()) } returns code
        val result = useCase("email", "associationId")
        assertEquals("email", result?.email)
        assertEquals("associationId", result?.associationId)
    }

    @Test
    fun invokeCodeTaken() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(associationsRepository, usersRepository)
        coEvery { usersRepository.getForEmail("email", false) } returns null
        coEvery { associationsRepository.createCodeInEmail("email", any(), "associationId", any()) } throws Exception()
        coEvery { associationsRepository.updateCodeInEmail("email", any(), "associationId", any()) } returns 1
        val result = useCase("email", "associationId")
        assertEquals("email", result?.email)
        assertEquals("associationId", result?.associationId)
    }

    @Test
    fun invokeInternalError() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(associationsRepository, usersRepository)
        coEvery { usersRepository.getForEmail("email", false) } returns null
        coEvery { associationsRepository.createCodeInEmail("email", any(), "associationId", any()) } throws Exception()
        coEvery { associationsRepository.updateCodeInEmail("email", any(), "associationId", any()) } returns 0
        val exception = assertFailsWith(ControllerException::class) {
            useCase("email", "associationId")
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun invokeEmailTaken() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(associationsRepository, usersRepository)
        coEvery { usersRepository.getForEmail("email", false) } returns User(
            "id", "associationId", "email", "password", "firstname", "lastname", false
        )
        assertEquals(null, useCase("email", "associationId"))
    }

}
