package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import com.suitebde.repositories.associations.IAssociationsRepository
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAssociationsUseCaseTest {

    @Test
    fun invokeTrue() = runBlocking {
        val associationRepository = mockk<IAssociationsRepository>()
        val useCase = GetAssociationsUseCase(associationRepository)
        val association = Association(
            UUID(), "name", "school", "city",
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
            UUID(), "name", "school", "city",
            true, Clock.System.now(), Clock.System.now()
        )
        coEvery { associationRepository.list() } returns listOf(association)
        assertEquals(listOf(association), useCase(false))
    }

}
