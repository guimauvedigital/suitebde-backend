package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import kotlin.test.Test

class DeleteCodeInEmailUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val useCase = DeleteCodeInEmailUseCase(associationsRepository)
        coEvery { associationsRepository.deleteCodeInEmail("code") } returns Unit
        useCase("code")
        coVerify { associationsRepository.deleteCodeInEmail("code") }
    }

}