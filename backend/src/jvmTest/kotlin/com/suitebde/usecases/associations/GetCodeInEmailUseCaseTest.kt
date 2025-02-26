package com.suitebde.usecases.associations

import com.suitebde.models.associations.CodeInEmail
import com.suitebde.repositories.associations.ICodesInEmailsRepository
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCodeInEmailUseCaseTest {

    private val now = Clock.System.now()
    private val tomorrow = now.plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
    private val yesterday = now.minus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<ICodesInEmailsRepository>()
        val useCase = GetCodeInEmailUseCase(repository)
        val code = CodeInEmail(
            "email", "code", UUID(), tomorrow
        )
        coEvery { repository.getCodeInEmail("code") } returns code
        assertEquals(code, useCase("code"))
    }

    @Test
    fun invokeExpired() = runBlocking {
        val repository = mockk<ICodesInEmailsRepository>()
        val useCase = GetCodeInEmailUseCase(repository)
        val code = CodeInEmail(
            "email", "code", UUID(), yesterday
        )
        coEvery { repository.getCodeInEmail("code") } returns code
        assertEquals(null, useCase("code"))
    }

    @Test
    fun invokeNone() = runBlocking {
        val repository = mockk<ICodesInEmailsRepository>()
        val useCase = GetCodeInEmailUseCase(repository)
        coEvery { repository.getCodeInEmail("code") } returns null
        assertEquals(null, useCase("code"))
    }

}
