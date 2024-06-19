package com.suitebde.controllers.root

import com.suitebde.models.associations.Association
import com.suitebde.models.web.WebPage
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetHomeWebPageUseCase
import dev.kaccelero.commons.responses.RedirectResponse
import dev.kaccelero.models.UUID
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class RootControllerTest {

    private val association = Association(
        UUID(), "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val homePage = WebPage(
        UUID(), UUID(), "home", "home", "content", true
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
