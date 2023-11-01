package me.nathanfallet.suitebde.usecases.users

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = UpdateUserUseCase(usersRepository, mockk())
        val user = User(
            "id", "name", "email", null,
            "first", "last", false
        )
        coEvery { usersRepository.updateUser(user) } returns 1
        assertEquals(user, useCase(user))
    }

    @Test
    fun invokeWithPassword() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val hashPasswordUseCase = mockk<IHashPasswordUseCase>()
        val useCase = UpdateUserUseCase(usersRepository, hashPasswordUseCase)
        val user = User(
            "id", "name", "email", "password",
            "first", "last", false
        )
        val hashedUser = user.copy(password = "hash")
        every { hashPasswordUseCase("password") } returns "hash"
        coEvery { usersRepository.updateUser(hashedUser) } returns 1
        assertEquals(hashedUser, useCase(user))
    }

    @Test
    fun invokeError() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = UpdateUserUseCase(usersRepository, mockk())
        val user = User(
            "id", "name", "email", null,
            "first", "last", false
        )
        coEvery { usersRepository.updateUser(user) } returns 0
        assertEquals(null, useCase(user))
    }

}