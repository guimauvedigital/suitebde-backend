package me.nathanfallet.suitebde.usecases.web

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.repositories.web.IWebMenusRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetWebMenusUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IWebMenusRepository>()
        val useCase = GetWebMenusUseCase(repository)
        val menu = WebMenu(
            "id", "associationId", "title", "url", 0
        )
        coEvery { repository.getMenus("associationId") } returns listOf(menu)
        assertEquals(listOf(menu), useCase("associationId"))
    }

}
