package me.nathanfallet.suitebde.usecases.associations

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAssociationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val associationsRepository = mockk<IAssociationsRepository>()
        val useCase = GetAssociationUseCase(associationsRepository)
        val association = Association(
            "id", "name", "school", "city",
            true, Clock.System.now(), Clock.System.now()
        )
        coEvery { associationsRepository.getAssociation("id") } returns association
        assertEquals(association, useCase("id"))
    }

}