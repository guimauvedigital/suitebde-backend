package me.nathanfallet.suitebde.usecases.auth

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import kotlin.test.Test

class DeleteCodeRegisterUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>()
        val useCase = DeleteCodeRegisterUseCase(deleteCodeInEmailUseCase)
        coEvery { deleteCodeInEmailUseCase("code") } returns Unit
        useCase(mockk(), "code")
        coVerify { deleteCodeInEmailUseCase("code") }
    }

}
