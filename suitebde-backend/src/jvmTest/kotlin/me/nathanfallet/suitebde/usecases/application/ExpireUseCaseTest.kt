package me.nathanfallet.suitebde.usecases.application

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.associations.ICodesInEmailsRepository
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import me.nathanfallet.usecases.models.delete.IDeleteModelSuspendUseCase
import kotlin.test.Test

class ExpireUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val codesInEmailsRepository = mockk<ICodesInEmailsRepository>()
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>(relaxed = true)
        val deleteAssociationUseCase = mockk<IDeleteModelSuspendUseCase<Association, String>>(relaxed = true)
        val useCase = ExpireUseCase(
            associationsRepository,
            codesInEmailsRepository,
            deleteCodeInEmailUseCase,
            deleteAssociationUseCase
        )
        val now = Clock.System.now()
        val code = CodeInEmail("email", "code", "association", now)
        val association = Association("id", "name", "school", "city", true, now, now)
        coEvery { codesInEmailsRepository.getCodesInEmailsExpiringBefore(now) } returns listOf(code)
        coEvery { associationsRepository.getAssociationsExpiringBefore(now) } returns listOf(association)
        useCase(now)
        coVerify { deleteCodeInEmailUseCase(code.code) }
        coVerify { deleteAssociationUseCase(association.id) }
    }

}
