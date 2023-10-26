package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAssociationsUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val useCase = GetAssociationsUseCase(associationRepository)
        val association = Association("id", "name")
        coEvery { associationRepository.getAssociations() } returns listOf(association)
        assertEquals(listOf(association), useCase(Unit))
    }

}