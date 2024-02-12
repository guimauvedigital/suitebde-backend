package me.nathanfallet.suitebde.usecases.application

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import kotlin.test.Test

class ExpireUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val codesInEmailsRepository = mockk<ICodesInEmailsRepository>()
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>(relaxed = true)
        val useCase = ExpireUseCase(
            codesInEmailsRepository,
            deleteCodeInEmailUseCase,
        )
        val now = Clock.System.now()
        val code = CodeInEmail("email", "code", "association", now)
        coEvery { codesInEmailsRepository.getCodesInEmailsExpiringBefore(now) } returns listOf(code)
        useCase(now)
        coVerify { deleteCodeInEmailUseCase(code.code) }
    }

}
