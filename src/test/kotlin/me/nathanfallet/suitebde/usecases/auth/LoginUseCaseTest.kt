package me.nathanfallet.suitebde.usecases.auth

import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val verifyPasswordUseCase = mockk<IVerifyPasswordUseCase>()
        val useCase = LoginUseCase(repository, verifyPasswordUseCase)
        val user = User("id", "association", "email", "hash", "first", "last", false)
        coEvery { repository.getUserForEmailInAssociation("email", "association", true) } returns user
        every { verifyPasswordUseCase.invoke(Pair("password", "hash")) } returns true
        assertEquals(user, useCase.invoke(Triple("email", "association", "password")))
    }

    @Test
    fun invokeNoUser() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val verifyPasswordUseCase = mockk<IVerifyPasswordUseCase>()
        val useCase = LoginUseCase(repository, verifyPasswordUseCase)
        coEvery { repository.getUserForEmailInAssociation("email", "association", true) } returns null
        val exception = assertThrows<ControllerException> {
            useCase.invoke(Triple("email", "association", "password"))
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals(LocalizedString.ERROR_AUTH_INVALID_CREDENTIALS, exception.error)
    }

    @Test
    fun invokeBadPassword() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val verifyPasswordUseCase = mockk<IVerifyPasswordUseCase>()
        val useCase = LoginUseCase(repository, verifyPasswordUseCase)
        val user = User("id", "association", "email", "hash", "first", "last", false)
        coEvery { repository.getUserForEmailInAssociation("email", "association", true) } returns user
        every { verifyPasswordUseCase.invoke(Pair("password", "hash")) } returns false
        val exception = assertThrows<ControllerException> {
            useCase.invoke(Triple("email", "association", "password"))
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals(LocalizedString.ERROR_AUTH_INVALID_CREDENTIALS, exception.error)
    }

}