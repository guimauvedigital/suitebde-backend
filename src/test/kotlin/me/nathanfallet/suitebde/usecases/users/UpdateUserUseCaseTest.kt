package me.nathanfallet.suitebde.usecases.users

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import kotlin.test.Test

class UpdateUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val usersRepository = mockk<IUsersRepository>()
        val useCase = UpdateUserUseCase(usersRepository)
        val user = User("id", "name", "email", "password", "first", "last", false)
        coEvery { usersRepository.updateUser(user) } returns Unit
        useCase(user)
        coVerify { usersRepository.updateUser(user) }
    }

}