package me.nathanfallet.suitebde.usecases.web

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPublicMenuForCallUseCaseTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val menu = WebMenu(
        "id", "associationId", "title", "url",
        0, null
    )

    @Test
    fun invoke() = runBlocking {
        val requireAssociationForCallUseCaseTest = mockk<IRequireAssociationForCallUseCase>()
        val getWebMenusUseCase = mockk<IListChildModelSuspendUseCase<WebMenu, String>>()
        val useCase = GetPublicMenuForCallUseCase(requireAssociationForCallUseCaseTest, getWebMenusUseCase)
        coEvery { requireAssociationForCallUseCaseTest.invoke(any()) } returns association
        coEvery { getWebMenusUseCase.invoke(association.id) } returns listOf(menu)
        assertEquals(listOf(menu), useCase(mockk()))
    }

}
