package me.nathanfallet.suitebde.usecases.web

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.repositories.web.IWebPagesRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class GetWebPageByUrlUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IWebPagesRepository>()
        val useCase = GetWebPageByUrlUseCase(repository)
        val page = WebPage(
            "id", "associationId", "url", "title", "content", true
        )
        coEvery { repository.getByUrl(page.url, "associationId") } returns page
        assertEquals(page, useCase.invoke(page.url, "associationId"))
    }

}
