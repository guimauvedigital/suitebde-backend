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
import me.nathanfallet.ktorx.controllers.base.IChildModelController
import me.nathanfallet.ktorx.controllers.base.IModelController
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.models.associations.*
import me.nathanfallet.suitebde.plugins.*
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.UnitModel
import org.jsoup.Jsoup
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DomainsInAssociationsRouterTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
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
                json(Serialization.json)
            }
        }
    }

    @Test
    fun testGetAllAPIv1() = testApplication {
        val client = installApp(this)
        val associationController =
            mockk<IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>>()
        val associationRouter = AssociationsRouter(associationController, mockk(), mockk(), mockk())
        val controller =
            mockk<IChildModelController<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, String>>()
        val router = DomainsInAssociationsRouter(controller, mockk(), mockk(), mockk(), associationRouter)
        coEvery { associationController.get(any(), UnitModel, "id") } returns association
        coEvery { controller.getAll(any(), association) } returns listOf(domain)
        routing {
            router.createRoutes(this)
        }
        assertEquals(listOf(domain), client.get("/api/v1/associations/id/domains").body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val associationController =
            mockk<IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>>()
        val associationRouter = AssociationsRouter(associationController, mockk(), mockk(), mockk())
        val controller =
            mockk<IChildModelController<DomainInAssociation, String, CreateDomainInAssociationPayload, Unit, Association, String>>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router =
            DomainsInAssociationsRouter(
                controller,
                getLocaleForCallUseCase,
                translateUseCase,
                getAdminMenuForCallUseCase,
                associationRouter
            )
        coEvery { associationController.get(any(), UnitModel, "id") } returns association
        coEvery { controller.getAll(any(), association) } returns listOf(domain)
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/associations/id/domains")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
    }

    @Test
    fun testCreateAdmin() = testApplication {
        val client = installApp(this)
        val associationController =
            mockk<IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>>()
        val associationRouter = AssociationsRouter(associationController, mockk(), mockk(), mockk())
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router =
            DomainsInAssociationsRouter(
                mockk(),
                getLocaleForCallUseCase,
                translateUseCase,
                getAdminMenuForCallUseCase,
                associationRouter
            )
        coEvery { associationController.get(any(), UnitModel, "id") } returns association
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/associations/id/domains/create")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("h6"))
    }

}
