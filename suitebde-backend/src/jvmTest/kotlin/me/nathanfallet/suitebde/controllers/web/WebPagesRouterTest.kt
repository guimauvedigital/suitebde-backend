package me.nathanfallet.suitebde.controllers.web

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
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.associations.AssociationForCallRouter
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.controllers.associations.IAssociationsController
import me.nathanfallet.suitebde.models.application.SuiteBDEJson
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.plugins.configureI18n
import me.nathanfallet.suitebde.plugins.configureSecurity
import me.nathanfallet.suitebde.plugins.configureSerialization
import me.nathanfallet.suitebde.plugins.configureTemplating
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetPublicMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import org.jsoup.Jsoup
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class WebPagesRouterTest {

    private val association = Association(
        "associationId", "name", "school", "city", true, Clock.System.now(), Clock.System.now()
    )
    private val page = WebPage(
        "id", "associationId", "url", "title", "content", false
    )

    private fun installApp(application: ApplicationTestBuilder): HttpClient {
        application.environment {
            config = ApplicationConfig("application.test.conf")
        }
        application.application {
            configureI18n()
            configureSerialization()
            configureSecurity()
            configureTemplating()
        }
        return application.createClient {
            followRedirects = false
            install(ContentNegotiation) {
                json(SuiteBDEJson.json)
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
            AssociationForCallRouter(mockk(), mockk()),
            AssociationsRouter(associationController, mockk(), mockk(), mockk())
        )
        coEvery { associationController.get(any(), "id") } returns association
        coEvery { controller.list(any(), association) } returns listOf(page)
        routing {
            router.createRoutes(this)
        }
        assertEquals(listOf(page), client.get("/api/v1/associations/id/webpages").body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IWebPagesController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            mockk(),
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.list(any(), association) } returns listOf(page)
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
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            mockk(),
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.get(any(), association, "id") } returns page
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/webpages/id/update")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h6"))
    }

    @Test
    fun testGetHomePublic() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IWebPagesController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getPublicMenuForCallUseCase = mockk<IGetPublicMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            getPublicMenuForCallUseCase,
            mockk(),
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.getHome(any(), association) } returns page
        coEvery { getPublicMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(page.title, document.getElementById("webpages_title")?.text())
    }

    @Test
    fun testGetByUrlPublic() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IWebPagesController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getPublicMenuForCallUseCase = mockk<IGetPublicMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            getPublicMenuForCallUseCase,
            mockk(),
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
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
        val getPublicMenuForCallUseCase = mockk<IGetPublicMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            getPublicMenuForCallUseCase,
            mockk(),
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
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
