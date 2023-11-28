package me.nathanfallet.suitebde.usecases.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RequireUserForCallUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val useCase = RequireUserForCallUseCase(getUserForCallUseCase)
        val call = mockk<ApplicationCall>()
        val user = User("id", "name", "email", "password", "first", "last", false)
        coEvery { getUserForCallUseCase(call) } returns user
        assertEquals(user, useCase(call))
    }

    @Test
    fun invokeFails() = runBlocking {
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val useCase = RequireUserForCallUseCase(getUserForCallUseCase)
        val call = mockk<ApplicationCall>()
        coEvery { getUserForCallUseCase(call) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            useCase(call)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals("auth_invalid_credentials", exception.key)
    }

}
