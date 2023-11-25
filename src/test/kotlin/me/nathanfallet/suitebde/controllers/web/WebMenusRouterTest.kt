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
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.controllers.associations.AssociationForCallRouter
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.web.CreateWebMenuPayload
import me.nathanfallet.suitebde.models.web.UpdateWebMenuPayload
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.plugins.*
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import org.jsoup.Jsoup
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class WebMenusRouterTest {

    private val association = Association(
        "associationId", "name", "school", "city", true, Clock.System.now(), Clock.System.now()
    )
    private val menu = WebMenu(
        "id", "associationId", "title", "url", 1
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
            mockk<IChildModelController<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, Association, String>>()
        val router = WebMenusRouter(
            controller, mockk(), mockk(), mockk(), AssociationForCallRouter(requireAssociationForCallUseCase, mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.getAll(any(), association) } returns listOf(menu)
        routing {
            router.createRoutes(this)
        }
        assertEquals(listOf(menu), client.get("/api/v1/webmenus").body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller =
            mockk<IChildModelController<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, Association, String>>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = WebMenusRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.getAll(any(), association) } returns listOf(menu)
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/webmenus")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
    }

    @Test
    fun testGetByIdAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller =
            mockk<IChildModelController<WebMenu, String, CreateWebMenuPayload, UpdateWebMenuPayload, Association, String>>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = WebMenusRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.get(any(), association, "id") } returns menu
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/webmenus/id/update")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h6"))
    }

}
