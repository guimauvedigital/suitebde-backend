package me.nathanfallet.suitebde.controllers.associations

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
import me.nathanfallet.suitebde.models.application.SuiteBDEJson
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.DomainInAssociation
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.plugins.configureI18n
import me.nathanfallet.suitebde.plugins.configureSecurity
import me.nathanfallet.suitebde.plugins.configureSerialization
import me.nathanfallet.suitebde.plugins.configureTemplating
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import org.jsoup.Jsoup
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DomainsInAssociationsRouterTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false
    )
    private val domain = DomainInAssociation("domain", "associationId")

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
        val associationRouter = AssociationsRouter(associationController, mockk(), mockk(), mockk(), mockk())
        val controller = mockk<IDomainsInAssociationsController>()
        val router = DomainsInAssociationsRouter(controller, mockk(), mockk(), mockk(), mockk(), associationRouter)
        coEvery { associationController.get(any(), "id") } returns association
        coEvery { controller.list(any(), association, null, null) } returns listOf(domain)
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/api/v1/associations/id/domains")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf(domain), response.body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val associationController = mockk<IAssociationsController>()
        val associationRouter = AssociationsRouter(associationController, mockk(), mockk(), mockk(), mockk())
        val controller = mockk<IDomainsInAssociationsController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = DomainsInAssociationsRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            requireUserForCallUseCase,
            getAdminMenuForCallUseCase,
            associationRouter
        )
        coEvery { associationController.get(any(), "id") } returns association
        coEvery { controller.list(any(), association, null, null) } returns listOf(domain)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/associations/id/domains")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
    }

    @Test
    fun testCreateAdmin() = testApplication {
        val client = installApp(this)
        val associationController = mockk<IAssociationsController>()
        val associationRouter = AssociationsRouter(associationController, mockk(), mockk(), mockk(), mockk())
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = DomainsInAssociationsRouter(
            mockk(),
            getLocaleForCallUseCase,
            translateUseCase,
            requireUserForCallUseCase,
            getAdminMenuForCallUseCase,
            associationRouter
        )
        coEvery { associationController.get(any(), "id") } returns association
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        coEvery { requireUserForCallUseCase(any()) } returns user
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/associations/id/domains/create")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("h2"))
    }

}
