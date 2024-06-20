package com.suitebde.usecases.web

import com.suitebde.models.web.WebPage
import com.suitebde.repositories.web.IWebPagesRepository
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class GetWebPageByUrlUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val repository = mockk<IWebPagesRepository>()
        val useCase = GetWebPageByUrlUseCase(repository)
        val page = WebPage(
            UUID(), UUID(), "url", "title", "content", true
        )
        coEvery { repository.getByUrl(page.url, page.associationId) } returns page
        assertEquals(page, useCase(page.url, page.associationId))
    }

}
