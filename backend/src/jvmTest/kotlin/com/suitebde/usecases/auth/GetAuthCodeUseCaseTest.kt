package com.suitebde.usecases.auth

import com.suitebde.models.application.Client
import com.suitebde.models.auth.ClientForUser
import com.suitebde.models.users.ClientInUser
import com.suitebde.models.users.User
import com.suitebde.repositories.users.IClientsInUsersRepository
import com.suitebde.usecases.users.IGetUserUseCase
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAuthCodeUseCaseTest {

    private val now = Clock.System.now()
    private val tomorrow = now.plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
    private val yesterday = now.minus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, UUID>>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val useCase = GetAuthCodeUseCase(repository, getClientUseCase, getUserUseCase)
        val clientForUser = ClientForUser(
            Client(UUID(), UUID(), "name", "description", "secret", "redirect"),
            User(UUID(), UUID(), "email", "password", "firstName", "lastName", false, Clock.System.now())
        )
        coEvery { repository.get("code") } returns ClientInUser(
            "code",
            clientForUser.user.id,
            clientForUser.client.id,
            tomorrow
        )
        coEvery { getClientUseCase(clientForUser.client.id) } returns clientForUser.client
        coEvery { getUserUseCase(clientForUser.user.id) } returns clientForUser.user
        assertEquals(clientForUser, useCase("code"))
    }

    @Test
    fun testInvokeNotFound() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val useCase = GetAuthCodeUseCase(repository, mockk(), mockk())
        coEvery { repository.get("code") } returns null
        assertEquals(null, useCase("code"))
    }

    @Test
    fun testInvokeExpired() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val useCase = GetAuthCodeUseCase(repository, mockk(), mockk())
        coEvery { repository.get("code") } returns ClientInUser("code", UUID(), UUID(), yesterday)
        assertEquals(null, useCase("code"))
    }

    @Test
    fun testInvokeBadClient() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, UUID>>()
        val useCase = GetAuthCodeUseCase(repository, getClientUseCase, mockk())
        val client = ClientInUser("code", UUID(), UUID(), tomorrow)
        coEvery { repository.get(client.code) } returns client
        coEvery { getClientUseCase(client.clientId) } returns null
        assertEquals(null, useCase("code"))
    }

    @Test
    fun testInvokeBadUser() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, UUID>>()
        val getUserUseCase = mockk<IGetUserUseCase>()
        val useCase = GetAuthCodeUseCase(repository, getClientUseCase, getUserUseCase)
        val clientForUser = ClientForUser(
            Client(UUID(), UUID(), "name", "description", "secret", "redirect"),
            User(UUID(), UUID(), "email", "password", "firstName", "lastName", false, Clock.System.now())
        )
        coEvery { repository.get("code") } returns ClientInUser(
            "code", clientForUser.user.id, clientForUser.client.id, tomorrow
        )
        coEvery { getClientUseCase(clientForUser.client.id) } returns clientForUser.client
        coEvery { getUserUseCase(clientForUser.user.id) } returns null
        assertEquals(null, useCase("code"))
    }

}
