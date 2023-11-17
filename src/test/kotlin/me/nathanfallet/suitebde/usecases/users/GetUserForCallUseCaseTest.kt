package me.nathanfallet.suitebde.usecases.users

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.auth.SessionPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.application.IGetSessionForCallUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserForCallUseCaseTest {

    @Test
    fun invokeWithNothing() = runBlocking {
        val getSessionForCallUseCase = mockk<IGetSessionForCallUseCase>()
        val useCase = GetUserForCallUseCase(getSessionForCallUseCase, mockk())
        val call = mockk<ApplicationCall>()
        every { call.principal<JWTPrincipal>() } returns null
        every { getSessionForCallUseCase(call) } returns null
        assertEquals(null, useCase(call))
    }

    @Test
    fun invokeWithBahPrincipal() = runBlocking {
        val getSessionForCallUseCase = mockk<IGetSessionForCallUseCase>()
        val useCase = GetUserForCallUseCase(getSessionForCallUseCase, mockk())
        val call = mockk<ApplicationCall>()
        every { call.principal<JWTPrincipal>()?.subject } returns null
        every { getSessionForCallUseCase(call) } returns null
        assertEquals(null, useCase(call))
    }

    @Test
    fun invokeWithJWT() = runBlocking {
        val getUserUseCase = mockk<IGetUserUseCase>()
        val useCase = GetUserForCallUseCase(mockk(), getUserUseCase)
        val call = mockk<ApplicationCall>()
        val user = User("id", "name", "email", "password", "first", "last", false)
        every { call.principal<JWTPrincipal>()?.subject } returns "id"
        coEvery { getUserUseCase("id") } returns user
        assertEquals(user, useCase(call))
    }

    @Test
    fun invokeWithSession() = runBlocking {
        val getUserUseCase = mockk<IGetUserUseCase>()
        val getSessionForCallUseCase = mockk<IGetSessionForCallUseCase>()
        val useCase = GetUserForCallUseCase(getSessionForCallUseCase, getUserUseCase)
        val call = mockk<ApplicationCall>()
        val user = User("id", "name", "email", "password", "first", "last", false)
        every { call.principal<JWTPrincipal>()?.subject } returns null
        every { getSessionForCallUseCase(call) } returns SessionPayload("id")
        coEvery { getUserUseCase("id") } returns user
        assertEquals(user, useCase(call))
    }

    @Test
    fun invokeWithNoUser() = runBlocking {
        val getUserUseCase = mockk<IGetUserUseCase>()
        val useCase = GetUserForCallUseCase(mockk(), getUserUseCase)
        val call = mockk<ApplicationCall>()
        every { call.principal<JWTPrincipal>()?.subject } returns "id"
        coEvery { getUserUseCase("id") } returns null
        assertEquals(null, useCase(call))
    }

}
