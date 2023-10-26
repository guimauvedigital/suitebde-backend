package me.nathanfallet.suitebde.usecases.users

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserForCallUseCaseTest {

    @Test
    fun invokeWithNothing() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = GetUserForCallUseCase(usersRepository)
        val call = mockk<ApplicationCall>()
        every { call.principal<JWTPrincipal>() } returns null
        assertEquals(null, useCase(call))
    }

    @Test
    fun invokeWithJWT() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = GetUserForCallUseCase(usersRepository)
        val call = mockk<ApplicationCall>()
        val user = User("id", "name", "email", "password", "first", "last", false)
        every { call.principal<JWTPrincipal>()?.subject } returns "id"
        coEvery { usersRepository.getUser("id") } returns user
        assertEquals(user, useCase(call))
    }

}