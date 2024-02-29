package me.nathanfallet.suitebde.usecases.auth

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.users.IGetUserForEmailUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val getUserForEmail = mockk<IGetUserForEmailUseCase>()
        val verifyPasswordUseCase = mockk<IVerifyPasswordUseCase>()
        val useCase = LoginUseCase(getUserForEmail, verifyPasswordUseCase)
        val user = User("id", "association", "email", "hash", "first", "last", false)
        coEvery { getUserForEmail("email", true) } returns user
        every { verifyPasswordUseCase("password", "hash") } returns true
        assertEquals(user, useCase(LoginPayload("email", "password")))
    }

    @Test
    fun invokeNoUser() = runBlocking {
        val getUserForEmail = mockk<IGetUserForEmailUseCase>()
        val verifyPasswordUseCase = mockk<IVerifyPasswordUseCase>()
        val useCase = LoginUseCase(getUserForEmail, verifyPasswordUseCase)
        coEvery { getUserForEmail("email", true) } returns null
        assertEquals(null, useCase(LoginPayload("email", "password")))
    }

    @Test
    fun invokeBadPassword() = runBlocking {
        val getUserForEmail = mockk<IGetUserForEmailUseCase>()
        val verifyPasswordUseCase = mockk<IVerifyPasswordUseCase>()
        val useCase = LoginUseCase(getUserForEmail, verifyPasswordUseCase)
        val user = User("id", "association", "email", "hash", "first", "last", false)
        coEvery { getUserForEmail("email", true) } returns user
        every { verifyPasswordUseCase("password", "hash") } returns false
        assertEquals(null, useCase(LoginPayload("email", "password")))
    }

}
