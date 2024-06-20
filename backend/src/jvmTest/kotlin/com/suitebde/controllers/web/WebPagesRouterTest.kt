package com.suitebde.controllers.web

import com.suitebde.controllers.associations.AssociationForCallRouter
import com.suitebde.controllers.associations.AssociationsRouter
import com.suitebde.controllers.associations.IAssociationsController
import com.suitebde.models.associations.Association
import com.suitebde.models.users.User
import com.suitebde.models.web.WebPage
import com.suitebde.plugins.*
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import com.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IGetUserForCallUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.createRoutes
import dev.kaccelero.serializers.Serialization
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.Clock
import org.jsoup.Jsoup
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class WebPagesRouterTest {

    private val association = Association(
        UUID(), "name", "school", "city", true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        UUID(), association.id, "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )
    private val page = WebPage(
        UUID(), association.id, "url", "title", "content", false
    )

    private fun installApp(application: ApplicationTestBuilder): HttpClient {
        application.environment {
            config = ApplicationConfig("application.test.conf")
        }
        application.application {
            configureI18n()
            configureKoin()
            configureSerialization()
            configureSecurity()
            configureTemplating()
        }
        return application.createClient {
            followRedirects = false
            install(ContentNegotiation) {
                json(Serialization.json)
            }
        }
    }

    @Test
    fun testGetAllAPIv1() = testApplication {
        val client = installApp(this)
        val associationController = mockk<IAssociationsController>()
        val controller = mockk<IWebPagesController>()
        val router = WebPagesRouter(
            controller,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            AssociationForCallRouter(mockk(), mockk()),
            AssociationsRouter(associationController, mockk(), mockk(), mockk(), mockk(), mockk())
        )
        coEvery { associationController.get(any(), association.id) } returns association
        coEvery { controller.list(any(), association, null, null) } returns listOf(page)
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/api/v1/associations/${association.id}/webpages")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf(page), response.body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IWebPagesController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            mockk(),
            requireUserForCallUseCase,
            getAssociationForCallUseCase,
            mockk(),
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.list(any(), association, null, null) } returns listOf(page)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { getAssociationForCallUseCase(any()) } returns association
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/webpages")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
    }

    @Test
    fun testGetByIdAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IWebPagesController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            mockk(),
            requireUserForCallUseCase,
            getAssociationForCallUseCase,
            mockk(),
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.get(any(), association, page.id) } returns page
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { getAssociationForCallUseCase(any()) } returns association
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/webpages/${page.id}/update")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h2"))
    }

    @Test
    fun testGetByUrlPublic() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IWebPagesController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val getPublicMenuForCallUseCase = mockk<IGetPublicMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            getUserForCallUseCase,
            mockk(),
            mockk(),
            getPublicMenuForCallUseCase,
            mockk(),
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        coEvery { controller.getByUrl(any(), association, page.url) } returns page
        coEvery { getPublicMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/pages/url")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(page.title, document.getElementById("webpages_title")?.text())
    }

    @Test
    fun testGetByUrlPublicNotFound() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IWebPagesController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val getPublicMenuForCallUseCase = mockk<IGetPublicMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            getUserForCallUseCase,
            mockk(),
            mockk(),
            getPublicMenuForCallUseCase,
            mockk(),
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        coEvery { controller.getByUrl(any(), association, "bad") } throws ControllerException(
            HttpStatusCode.NotFound,
            "webpages_not_found"
        )
        coEvery { getPublicMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/pages/bad")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

}
