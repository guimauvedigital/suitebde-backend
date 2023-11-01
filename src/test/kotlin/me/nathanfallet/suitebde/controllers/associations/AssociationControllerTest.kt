package me.nathanfallet.suitebde.controllers.associations

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationsUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
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
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationsUseCase(true) } returns listOf(association)
        coEvery { getUserForCallUseCase(any()) } returns null
        val controller = AssociationController(
            getAssociationsUseCase, getUserForCallUseCase, mockk(), mockk(), mockk()
        )
        assertEquals(listOf(association), controller.getAll(mockk()))
    }

}