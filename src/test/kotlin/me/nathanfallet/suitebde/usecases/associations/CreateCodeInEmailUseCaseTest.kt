package me.nathanfallet.suitebde.usecases.associations

import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.IUsersRepository
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateCodeInEmailUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(associationsRepository, usersRepository)
        val code = CodeInEmail(
            "email", "code", "associationId", Clock.System.now()
        )
        coEvery { usersRepository.getUserForEmail("email", false) } returns null
        coEvery { associationsRepository.createCodeInEmail("email", any(), "associationId", any()) } returns code
        val result = useCase.invoke(Pair("email", "associationId"))
        assertEquals("email", result?.email)
        assertEquals("associationId", result?.associationId)
    }

    @Test
    fun invokeCodeTaken() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(associationsRepository, usersRepository)
        coEvery { usersRepository.getUserForEmail("email", false) } returns null
        coEvery { associationsRepository.createCodeInEmail("email", any(), "associationId", any()) } throws Exception()
        coEvery { associationsRepository.updateCodeInEmail("email", any(), "associationId", any()) } returns 1
        val result = useCase.invoke(Pair("email", "associationId"))
        assertEquals("email", result?.email)
        assertEquals("associationId", result?.associationId)
    }

    @Test
    fun invokeInternalError() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(associationsRepository, usersRepository)
        coEvery { usersRepository.getUserForEmail("email", false) } returns null
        coEvery { associationsRepository.createCodeInEmail("email", any(), "associationId", any()) } throws Exception()
        coEvery { associationsRepository.updateCodeInEmail("email", any(), "associationId", any()) } returns 0
        val exception = assertThrows<ControllerException> {
            useCase.invoke(Pair("email", "associationId"))
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals(LocalizedString.ERROR_INTERNAL, exception.error)
    }

    @Test
    fun invokeEmailTaken() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val usersRepository = mockk<IUsersRepository>()
        val useCase = CreateCodeInEmailUseCase(associationsRepository, usersRepository)
        coEvery { usersRepository.getUserForEmail("email", false) } returns User(
            "id", "associationId", "email", "password", "firstname", "lastname", false
        )
        assertEquals(null, useCase.invoke(Pair("email", "associationId")))
    }

}