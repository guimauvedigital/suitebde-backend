package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAssociationsUseCaseTest {

    @Test
    fun invokeTrue() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val useCase = GetAssociationsUseCase(associationRepository)
        val association = Association(
            "id", "name", "school", "city",
            true, Clock.System.now(), Clock.System.now()
        )
        coEvery { associationRepository.getValidatedAssociations() } returns listOf(association)
        assertEquals(listOf(association), useCase(true))
    }

    @Test
    fun invokeFalse() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val useCase = GetAssociationsUseCase(associationRepository)
        val association = Association(
            "id", "name", "school", "city",
            true, Clock.System.now(), Clock.System.now()
        )
        coEvery { associationRepository.getAssociations() } returns listOf(association)
        assertEquals(listOf(association), useCase(false))
    }

}