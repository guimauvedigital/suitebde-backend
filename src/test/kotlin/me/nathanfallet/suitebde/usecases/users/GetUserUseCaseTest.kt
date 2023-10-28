package me.nathanfallet.suitebde.usecases.users

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = GetUserUseCase(usersRepository)
        val user = User("id", "name", "email", "password", "first", "last", false)
        coEvery { usersRepository.getUser("id") } returns user
        assertEquals(user, useCase("id"))
    }

}