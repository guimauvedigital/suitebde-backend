package com.suitebde.usecases.users

import com.suitebde.models.users.User
import com.suitebde.repositories.users.IUsersRepository
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val useCase = GetUserUseCase(repository)
        val user = User(UUID(), UUID(), "email", "password", "first", "last", false, Clock.System.now())
        coEvery { repository.get(user.id) } returns user
        assertEquals(user, useCase(user.id))
    }

    @Test
    fun invokeNotFound() = runBlocking {
        val repository = mockk<IUsersRepository>()
        val useCase = GetUserUseCase(repository)
        val id = UUID()
        coEvery { repository.get(id) } returns null
        assertEquals(null, useCase(id))
    }

}
