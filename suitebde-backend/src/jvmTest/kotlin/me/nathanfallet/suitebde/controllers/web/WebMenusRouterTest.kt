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
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.controllers.associations.AssociationForCallRouter
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.controllers.associations.IAssociationsController
import me.nathanfallet.suitebde.models.application.SuiteBDEJson
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.plugins.configureI18n
import me.nathanfallet.suitebde.plugins.configureSecurity
import me.nathanfallet.suitebde.plugins.configureSerialization
import me.nathanfallet.suitebde.plugins.configureTemplating
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
    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false
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
                json(SuiteBDEJson.json)
            }
        }
    }

    @Test
    fun testGetAllAPIv1() = testApplication {
        val client = installApp(this)
        val associationController = mockk<IAssociationsController>()
        val controller = mockk<IWebMenusController>()
        val router = WebMenusRouter(
            controller,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            AssociationForCallRouter(mockk(), mockk()),
            AssociationsRouter(associationController, mockk(), mockk(), mockk(), mockk())
        )
        coEvery { associationController.get(any(), "id") } returns association
        coEvery { controller.list(any(), association) } returns listOf(menu)
        routing {
            router.createRoutes(this)
        }
        assertEquals(listOf(menu), client.get("/api/v1/associations/id/webmenus").body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IWebMenusController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = WebMenusRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            requireUserForCallUseCase,
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.list(any(), association) } returns listOf(menu)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/webmenus")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
    }

    @Test
    fun testGetByIdAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IWebMenusController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = WebMenusRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            requireUserForCallUseCase,
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.get(any(), association, "id") } returns menu
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/webmenus/id/update")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h2"))
    }

}
