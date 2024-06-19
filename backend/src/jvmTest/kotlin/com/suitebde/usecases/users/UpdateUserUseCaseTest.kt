package com.suitebde.usecases.users

import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import com.suitebde.repositories.users.IUsersRepository
import com.suitebde.usecases.auth.IHashPasswordUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val useCase = UpdateUserUseCase(usersRepository, mockk(), getUserUseCase)
        val user = User(
            UUID(), UUID(), "email", null,
            "first", "last", false, Clock.System.now()
        )
        val payload = UpdateUserPayload("first", "last", null)
        coEvery { usersRepository.update(user.id, payload, UUID()) } returns true
        coEvery { getUserUseCase(user.id) } returns user
        assertEquals(user, useCase(user.id, payload, UUID()))
    }

    @Test
    fun invokeWithPassword() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val hashPasswordUseCase = mockk<IHashPasswordUseCase>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val useCase = UpdateUserUseCase(usersRepository, hashPasswordUseCase, getUserUseCase)
        val user = User(
            UUID(), UUID(), "email", "password",
            "first", "last", false, Clock.System.now()
        )
        val payload = UpdateUserPayload("first", "last", "password")
        val hashedUser = user.copy(password = "hash")
        val hashedPayload = payload.copy(password = "hash")
        every { hashPasswordUseCase("password") } returns "hash"
        coEvery { usersRepository.update(user.id, hashedPayload, UUID()) } returns true
        coEvery { getUserUseCase(user.id) } returns hashedUser
        assertEquals(hashedUser, useCase(user.id, payload, UUID()))
    }

    @Test
    fun invokeError() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = UpdateUserUseCase(usersRepository, mockk(), mockk())
        val user = User(
            UUID(), UUID(), "email", null,
            "first", "last", false, Clock.System.now()
        )
        val payload = UpdateUserPayload("first", "last", null)
        coEvery { usersRepository.update(user.id, payload, UUID()) } returns false
        assertEquals(null, useCase(user.id, payload, UUID()))
    }

}
