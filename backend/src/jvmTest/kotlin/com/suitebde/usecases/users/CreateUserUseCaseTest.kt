package com.suitebde.usecases.users

import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.User
import com.suitebde.repositories.users.IUsersRepository
import com.suitebde.usecases.auth.IHashPasswordUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val hashPasswordUseCase = mockk<IHashPasswordUseCase>()
        val useCase = CreateUserUseCase(usersRepository, hashPasswordUseCase)
        val user = User(
            UUID(), UUID(), "email", null,
            "firstName", "lastName", true, Clock.System.now()
        )
        every { hashPasswordUseCase(any()) } returns "hash"
        coEvery { usersRepository.create(any(), user.associationId) } returns user
        assertEquals(
            user, useCase(
                CreateUserPayload(
                    "email", "password",
                    "firstName", "lastName", true
                ),
                user.associationId
            )
        )
        coVerifyOrder {
            usersRepository.create(
                CreateUserPayload(
                    "email", "hash",
                    "firstName", "lastName", true
                ),
                user.associationId
            )
        }
    }

    @Test
    fun invokeWithNull() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val hashPasswordUseCase = mockk<IHashPasswordUseCase>()
        val useCase = CreateUserUseCase(usersRepository, hashPasswordUseCase)
        val associationId = UUID()
        every { hashPasswordUseCase(any()) } returns "hash"
        coEvery { usersRepository.create(any(), associationId) } returns null
        assertEquals(
            null, useCase(
                CreateUserPayload(
                    "email", "password",
                    "firstName", "lastName", true
                ),
                associationId
            )
        )
    }

}
