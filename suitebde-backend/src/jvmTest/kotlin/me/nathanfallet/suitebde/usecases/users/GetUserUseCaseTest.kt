package me.nathanfallet.suitebde.usecases.users

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.users.IUsersRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val useCase = GetUserUseCase(repository)
        val user = User("id", "name", "email", "password", "first", "last", false, Clock.System.now())
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
