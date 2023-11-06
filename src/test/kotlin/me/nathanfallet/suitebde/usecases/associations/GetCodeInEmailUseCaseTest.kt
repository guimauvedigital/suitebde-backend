package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCodeInEmailUseCaseTest {

    private val now = Clock.System.now()
    private val tomorrow = now.plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
    private val yesterday = now.minus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IAssociationsRepository>()
        val useCase = GetCodeInEmailUseCase(repository)
        val code = CodeInEmail(
            "email", "code", "associationId", tomorrow
        )
        coEvery { repository.getCodeInEmail("code") } returns code
        assertEquals(code, useCase("code", now))
    }

    @Test
    fun invokeExpired() = runBlocking {
        val repository = mockk<IAssociationsRepository>()
        val useCase = GetCodeInEmailUseCase(repository)
        val code = CodeInEmail(
            "email", "code", "associationId", yesterday
        )
        coEvery { repository.getCodeInEmail("code") } returns code
        assertEquals(null, useCase("code", now))
    }

    @Test
    fun invokeNone() = runBlocking {
        val repository = mockk<IAssociationsRepository>()
        val useCase = GetCodeInEmailUseCase(repository)
        coEvery { repository.getCodeInEmail("code") } returns null
        assertEquals(null, useCase("code", now))
    }

}