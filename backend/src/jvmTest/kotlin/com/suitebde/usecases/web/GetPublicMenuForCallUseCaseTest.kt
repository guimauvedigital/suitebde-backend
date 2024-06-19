package com.suitebde.usecases.web

import com.suitebde.models.associations.Association
import com.suitebde.models.web.WebMenu
import com.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPublicMenuForCallUseCaseTest {

    private val association = Association(
        UUID(), "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val menu = WebMenu(
        UUID(), UUID(), "title", "url",
        0, null
    )

    @Test
    fun invoke() = runBlocking {
        val requireAssociationForCallUseCaseTest = mockk<IRequireAssociationForCallUseCase>()
        val getWebMenusUseCase = mockk<IListChildModelSuspendUseCase<WebMenu, UUID>>()
        val useCase = GetPublicMenuForCallUseCase(requireAssociationForCallUseCaseTest, getWebMenusUseCase)
        coEvery { requireAssociationForCallUseCaseTest.invoke(any()) } returns association
        coEvery { getWebMenusUseCase.invoke(association.id) } returns listOf(menu)
        assertEquals(listOf(menu), useCase(mockk()))
    }

}
