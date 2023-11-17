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
import me.nathanfallet.ktorx.controllers.base.IChildModelController
import me.nathanfallet.suitebde.controllers.associations.AssociationForCallRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.web.CreateWebPagePayload
import me.nathanfallet.suitebde.models.web.UpdateWebPagePayload
import me.nathanfallet.suitebde.models.web.WebPage
import me.nathanfallet.suitebde.plugins.*
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import org.jsoup.Jsoup
import kotlin.test.Test
import kotlin.test.assertEquals

class WebPagesRouterTest {

    private val user = User(
        "id", "associationId", "email", null, "firstname", "lastname", false
    )
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
                json(Serialization.json)
            }
        }
    }

    @Test
    fun testGetAllAPIv1() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller =
            mockk<IChildModelController<WebPage, String, CreateWebPagePayload, UpdateWebPagePayload, Association, String>>()
        val router = WebPagesRouter(
            controller, mockk(), mockk(), AssociationForCallRouter(requireAssociationForCallUseCase, mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.getAll(any(), association) } returns listOf(page)
        routing {
            router.createRoutes(this)
        }
        assertEquals(listOf(page), client.get("/api/v1/webpages").body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller =
            mockk<IChildModelController<WebPage, String, CreateWebPagePayload, UpdateWebPagePayload, Association, String>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            translateUseCase,
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.getAll(any(), association) } returns listOf(page)
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf()
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/webpages")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
    }

    @Test
    fun testGetByIdAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller =
            mockk<IChildModelController<WebPage, String, CreateWebPagePayload, UpdateWebPagePayload, Association, String>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = WebPagesRouter(
            controller,
            translateUseCase,
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.get(any(), association, "id") } returns page
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf()
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/webpages/id/update")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h6"))
    }

}
