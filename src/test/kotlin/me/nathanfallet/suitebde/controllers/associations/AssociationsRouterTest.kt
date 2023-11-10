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
import me.nathanfallet.ktor.routers.controllers.base.IModelController
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.associations.UpdateAssociationPayload
import me.nathanfallet.suitebde.plugins.*
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.models.UnitModel
import org.jsoup.Jsoup
import kotlin.test.Test
import kotlin.test.assertEquals

class AssociationsRouterTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
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
        val controller =
            mockk<IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>>()
        val router = AssociationsRouter(controller, mockk(), mockk())
        coEvery { controller.getAll(any(), UnitModel) } returns listOf(association)
        routing {
            router.createRoutes(this)
        }
        assertEquals(listOf(association), client.get("/api/v1/associations").body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val controller =
            mockk<IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = AssociationsRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { controller.getAll(any(), UnitModel) } returns listOf(association)
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf()
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/associations")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
    }

    @Test
    fun testGetByIdAdmin() = testApplication {
        val client = installApp(this)
        val controller =
            mockk<IModelController<Association, String, CreateAssociationPayload, UpdateAssociationPayload>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = AssociationsRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { controller.get(any(), UnitModel, "id") } returns association
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf()
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/associations/id")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h6"))
    }

    @Test
    fun testCreateAdmin() = testApplication {
        val client = installApp(this)
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = AssociationsRouter(mockk(), translateUseCase, getAdminMenuForCallUseCase)
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf()
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/associations/create")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("h6"))
    }

}
