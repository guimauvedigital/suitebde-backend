package me.nathanfallet.suitebde.controllers.associations

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationsUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class AssociationControllerTest {

    private val association = Association(
        "id", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )

    @Test
    fun testGetAll() = runBlocking {
        val getAssociationsUseCase = mockk<IGetAssociationsUseCase>()
        coEvery { getAssociationsUseCase(true) } returns listOf(association)
        val controller = AssociationController(getAssociationsUseCase)
        assertEquals(listOf(association), controller.getAll())
    }

}