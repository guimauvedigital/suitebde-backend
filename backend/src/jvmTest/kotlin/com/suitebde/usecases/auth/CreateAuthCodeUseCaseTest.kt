package com.suitebde.usecases.auth

import com.suitebde.models.application.Client
import com.suitebde.models.auth.ClientForUser
import com.suitebde.models.users.ClientInUser
import com.suitebde.models.users.User
import com.suitebde.repositories.users.IClientsInUsersRepository
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateAuthCodeUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val useCase = CreateAuthCodeUseCase(repository)
        val clientId = UUID()
        val userId = UUID()
        coEvery { repository.create(userId, clientId, any()) } returns ClientInUser(
            "code", UUID(), UUID(), Clock.System.now()
        )
        assertEquals(
            "code",
            useCase(
                ClientForUser(
                    Client(clientId, UUID(), "name", "description", "secret", "redirect"),
                    User(
                        userId, UUID(), "email", "password",
                        "firstName", "lastName", false, Clock.System.now()
                    )
                )
            )
        )
    }

    @Test
    fun testInvokeError() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val useCase = CreateAuthCodeUseCase(repository)
        val clientId = UUID()
        val userId = UUID()
        coEvery { repository.create(userId, clientId, any()) } returns null
        assertEquals(
            null,
            useCase(
                ClientForUser(
                    Client(clientId, UUID(), "name", "description", "secret", "redirect"),
                    User(
                        userId, UUID(), "email", "password",
                        "firstName", "lastName", false, Clock.System.now()
                    )
                )
            )
        )
    }

}
