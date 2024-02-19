package me.nathanfallet.suitebde.controllers.root

import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.responses.RedirectResponse
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetHomeWebPageUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class RootControllerTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val homePage = WebPage(
        "homePage", "associationId", "home", "home", "content", true
    )

    @Test
    fun testRedirectAssociationHome() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getHomeWebPageUseCase = mockk<IGetHomeWebPageUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = RootController(getAssociationForCallUseCase, getHomeWebPageUseCase)
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getHomeWebPageUseCase(homePage.associationId) } returns homePage
        assertEquals(RedirectResponse("/pages/home"), controller.redirect(call))
    }

    @Test
    fun testRedirectAssociationHomeNotFound() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getHomeWebPageUseCase = mockk<IGetHomeWebPageUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = RootController(getAssociationForCallUseCase, getHomeWebPageUseCase)
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { getHomeWebPageUseCase(homePage.associationId) } returns null
        assertEquals(RedirectResponse("/home"), controller.redirect(call))
    }

    @Test
    fun testRedirectNoAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = RootController(getAssociationForCallUseCase, mockk())
        coEvery { getAssociationForCallUseCase(call) } returns null
        assertEquals(RedirectResponse("/home"), controller.redirect(call))
    }

}
