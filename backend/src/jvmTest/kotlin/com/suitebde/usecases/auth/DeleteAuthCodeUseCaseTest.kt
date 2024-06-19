package com.suitebde.usecases.auth

import com.suitebde.repositories.users.IClientsInUsersRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteAuthCodeUseCaseTest {

    @Test
    fun testInvokeTrue() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val useCase = DeleteAuthCodeUseCase(repository)
        coEvery { repository.delete("code") } returns true
        assertEquals(true, useCase("code"))
    }

    @Test
    fun testInvokeFalse() = runBlocking {
        val repository = mockk<IClientsInUsersRepository>()
        val useCase = DeleteAuthCodeUseCase(repository)
        coEvery { repository.delete("code") } returns false
        assertEquals(false, useCase("code"))
    }

}
