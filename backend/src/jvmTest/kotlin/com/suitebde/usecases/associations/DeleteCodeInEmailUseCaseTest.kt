package com.suitebde.usecases.associations

import com.suitebde.repositories.associations.ICodesInEmailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class DeleteCodeInEmailUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val codesInEmailsRepository = mockk<ICodesInEmailsRepository>()
        val useCase = DeleteCodeInEmailUseCase(codesInEmailsRepository)
        coEvery { codesInEmailsRepository.deleteCodeInEmail("code") } returns Unit
        useCase("code")
        coVerify { codesInEmailsRepository.deleteCodeInEmail("code") }
    }

}
