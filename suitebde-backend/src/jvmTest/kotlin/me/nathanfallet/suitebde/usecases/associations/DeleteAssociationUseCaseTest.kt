package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import kotlin.test.Test

class DeleteAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val repository = mockk<IAssociationsRepository>()
        val useCase = DeleteAssociationUseCase(repository)
        val now = Clock.System.now()
        val association = Association("id", "name", "school", "city", true, now, now)
        coEvery { repository.delete(association.id) } returns true
        useCase(association.id)
        coVerify { repository.delete(association.id) }
    }

}