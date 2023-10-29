package me.nathanfallet.suitebde.usecases.auth

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val verifyPasswordUseCase = mockk<IVerifyPasswordUseCase>()
        val useCase = LoginUseCase(repository, verifyPasswordUseCase)
        val user = User("id", "association", "email", "hash", "first", "last", false)
        coEvery { repository.getUserForEmail("email", true) } returns user
        every { verifyPasswordUseCase.invoke(Pair("password", "hash")) } returns true
        assertEquals(user, useCase.invoke(LoginPayload("email", "password")))
    }

    @Test
    fun invokeNoUser() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val verifyPasswordUseCase = mockk<IVerifyPasswordUseCase>()
        val useCase = LoginUseCase(repository, verifyPasswordUseCase)
        coEvery { repository.getUserForEmail("email", true) } returns null
        assertEquals(null, useCase.invoke(LoginPayload("email", "password")))
    }

    @Test
    fun invokeBadPassword() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val verifyPasswordUseCase = mockk<IVerifyPasswordUseCase>()
        val useCase = LoginUseCase(repository, verifyPasswordUseCase)
        val user = User("id", "association", "email", "hash", "first", "last", false)
        coEvery { repository.getUserForEmail("email", true) } returns user
        every { verifyPasswordUseCase.invoke(Pair("password", "hash")) } returns false
        assertEquals(null, useCase.invoke(LoginPayload("email", "password")))
    }

}