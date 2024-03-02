package me.nathanfallet.suitebde.usecases.users

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val useCase = UpdateUserUseCase(usersRepository, mockk(), getUserUseCase)
        val user = User(
            "id", "associationId", "email", null,
            "first", "last", false
        )
        val payload = UpdateUserPayload("first", "last", null)
        coEvery { usersRepository.update(user.id, payload, "associationId") } returns true
        coEvery { getUserUseCase(user.id) } returns user
        assertEquals(user, useCase(user.id, payload, "associationId"))
    }

    @Test
    fun invokeWithPassword() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val hashPasswordUseCase = mockk<IHashPasswordUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val useCase = UpdateUserUseCase(usersRepository, hashPasswordUseCase, getUserUseCase)
        val user = User(
            "id", "associationId", "email", "password",
            "first", "last", false
        )
        val payload = UpdateUserPayload("first", "last", "password")
        val hashedUser = user.copy(password = "hash")
        val hashedPayload = payload.copy(password = "hash")
        every { hashPasswordUseCase("password") } returns "hash"
        coEvery { usersRepository.update(user.id, hashedPayload, "associationId") } returns true
        coEvery { getUserUseCase(user.id) } returns hashedUser
        assertEquals(hashedUser, useCase(user.id, payload, "associationId"))
    }

    @Test
    fun invokeError() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = UpdateUserUseCase(usersRepository, mockk(), mockk())
        val user = User(
            "id", "associationId", "email", null,
            "first", "last", false
        )
        val payload = UpdateUserPayload("first", "last", null)
        coEvery { usersRepository.update(user.id, payload, "associationId") } returns false
        assertEquals(null, useCase(user.id, payload, "associationId"))
    }

}
