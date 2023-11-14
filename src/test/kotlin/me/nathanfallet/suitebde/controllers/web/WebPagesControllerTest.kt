package me.nathanfallet.suitebde.controllers.web

import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.usecases.models.get.IGetChildModelSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WebPagesControllerTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val page = WebPage(
        "id", "associationId", "title", "content", false
    )
    private val homePage = WebPage(
        "homePage", "associationId", "home", "content", true
    )

    @Test
    fun testGet() = runBlocking {
        val getWebPageUseCase = mockk<IGetChildModelSuspendUseCase<WebPage, String, String>>()
        val controller = WebPagesController(getWebPageUseCase)
        coEvery { getWebPageUseCase(page.id, page.associationId) } returns page
        assertEquals(page, controller.get(mockk(), association, page.id))
    }

    @Test
    fun testGetNotFound() = runBlocking {
        val getWebPageUseCase = mockk<IGetChildModelSuspendUseCase<WebPage, String, String>>()
        val controller = WebPagesController(getWebPageUseCase)
        coEvery { getWebPageUseCase(page.id, page.associationId) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.get(mockk(), association, page.id)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("web_pages_not_found", exception.key)
    }

}
