package me.nathanfallet.suitebde.usecases.users

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateUserUseCaseTest {

    @Test
    fun invokeTrue() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = UpdateUserUseCase(usersRepository)
        val user = User("id", "name", "email", "password", "first", "last", false)
        coEvery { usersRepository.updateUser(user) } returns 1
        assertEquals(true, useCase(user))
    }

    @Test
    fun invokeFalse() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = UpdateUserUseCase(usersRepository)
        val user = User("id", "name", "email", "password", "first", "last", false)
        coEvery { usersRepository.updateUser(user) } returns 0
        assertEquals(false, useCase(user))
    }

}