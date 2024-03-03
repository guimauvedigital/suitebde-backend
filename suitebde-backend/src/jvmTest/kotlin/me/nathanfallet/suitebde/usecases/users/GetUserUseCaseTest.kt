package me.nathanfallet.suitebde.usecases.users

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val useCase = GetUserUseCase(repository)
        val user = User("id", "name", "email", "password", "first", "last", false)
        coEvery { repository.get("id") } returns user
        assertEquals(user, useCase("id"))
    }

    @Test
    fun invokeNotFound() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val useCase = GetUserUseCase(repository)
        coEvery { repository.get("id") } returns null
        assertEquals(null, useCase("id"))
    }

}
