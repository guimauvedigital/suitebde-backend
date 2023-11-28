package me.nathanfallet.suitebde.usecases.web

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetHomeWebPageUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IWebPagesRepository>()
        val useCase = GetHomeWebPageUseCase(repository)
        val page = WebPage(
            "id", "associationId", "url", "title", "content", true
        )
        coEvery { repository.getHome("associationId") } returns page
        assertEquals(page, useCase("associationId"))
    }

}
