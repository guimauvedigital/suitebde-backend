package me.nathanfallet.suitebde.usecases.users

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val hashPasswordUseCase = mockk<IHashPasswordUseCase>()
        val now = Clock.System.now()
        val useCase = CreateUserUseCase(usersRepository, hashPasswordUseCase)
        val user = User(
            "id", "associationId", "email", null,
            "firstName", "lastName", true
        )
        every { hashPasswordUseCase(any()) } returns "hash"
        coEvery { usersRepository.create(any()) } returns user
        assertEquals(
            user, useCase(
                CreateUserPayload(
                    "associationId", "email", "password",
                    "firstName", "lastName", true
                )
            )
        )
        coVerifyOrder {
            usersRepository.create(
                CreateUserPayload(
                "associationId", "email", "hash",
                "firstName", "lastName", true
                )
            )
        }
    }

    @Test
    fun invokeWithNull() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val hashPasswordUseCase = mockk<IHashPasswordUseCase>()
        val useCase = CreateUserUseCase(usersRepository, hashPasswordUseCase)
        every { hashPasswordUseCase(any()) } returns "hash"
        coEvery { usersRepository.create(any()) } returns null
        assertEquals(
            null, useCase(
                CreateUserPayload(
                    "associationId", "email", "password",
                    "firstName", "lastName", true
                )
            )
        )
    }

}