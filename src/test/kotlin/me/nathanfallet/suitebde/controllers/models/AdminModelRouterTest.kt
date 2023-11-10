package me.nathanfallet.suitebde.controllers.models

import io.ktor.client.*
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
import me.nathanfallet.ktor.routers.controllers.base.IModelController
import me.nathanfallet.ktor.routers.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.plugins.*
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.models.UnitModel
import org.jsoup.Jsoup
import kotlin.test.Test
import kotlin.test.assertEquals

class AdminModelRouterTest {

    private val mock = ModelRouterTestModel("id", "string")

    private val menuItem = WebMenu("id", "associationId", "title", "url")

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

    private fun createRouter(
        controller: IModelController<ModelRouterTestModel, String, ModelRouterTestModel, ModelRouterTestModel>,
        translateUseCase: ITranslateUseCase,
        getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
    ): AdminModelRouter<ModelRouterTestModel, String, ModelRouterTestModel, ModelRouterTestModel> {
        return AdminModelRouter(
            ModelRouterTestModel::class,
            ModelRouterTestModel::class,
            ModelRouterTestModel::class,
            controller,
            translateUseCase,
            getAdminMenuForCallUseCase
        )
    }

    @Test
    fun testAdminGetRoute() = testApplication {
        val client = installApp(this)
        val controller =
            mockk<IModelController<ModelRouterTestModel, String, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = createRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { controller.getAll(any(), UnitModel) } returns listOf(mock)
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf(menuItem)
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/modelroutertestmodels")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
        assertEquals(true, document.getElementById("th_id")?.`is`("th"))
        assertEquals(true, document.getElementById("th_string")?.`is`("th"))
    }

    @Test
    fun testAdminGetRouteForbidden() = testApplication {
        val client = installApp(this)
        val controller =
            mockk<IModelController<ModelRouterTestModel, String, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = createRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf(menuItem)
        coEvery { controller.getAll(any(), UnitModel) } throws ControllerException(
            HttpStatusCode.Forbidden,
            "error_mock"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/modelroutertestmodels")
        assertEquals(HttpStatusCode.Forbidden, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("403", document.getElementById("number")?.text())
    }

    @Test
    fun testAdminGetRouteUnauthorized() = testApplication {
        val client = installApp(this)
        val controller =
            mockk<IModelController<ModelRouterTestModel, String, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = createRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf(menuItem)
        coEvery { controller.getAll(any(), UnitModel) } throws ControllerException(
            HttpStatusCode.Unauthorized,
            "error_mock"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/modelroutertestmodels")
        assertEquals(HttpStatusCode.Found, response.status)
    }

    @Test
    fun testAdminGetIdRoute() = testApplication {
        val client = installApp(this)
        val controller =
            mockk<IModelController<ModelRouterTestModel, String, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = createRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { controller.get(any(), UnitModel, "id") } returns mock
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf(menuItem)
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/modelroutertestmodels/id")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h6"))
        assertEquals(null, document.getElementById("id"))
        assertEquals(true, document.getElementById("string")?.`is`("input"))
    }

    @Test
    fun testAdminGetIdRouteForbidden() = testApplication {
        val client = installApp(this)
        val controller =
            mockk<IModelController<ModelRouterTestModel, String, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = createRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { controller.get(any(), UnitModel, "id") } throws ControllerException(
            HttpStatusCode.Forbidden,
            "error_mock"
        )
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf(menuItem)
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/modelroutertestmodels/id")
        assertEquals(HttpStatusCode.Forbidden, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("403", document.getElementById("number")?.text())
    }

    @Test
    fun testAdminPostIdRoute() = testApplication {
        val client = installApp(this)
        val controller =
            mockk<IModelController<ModelRouterTestModel, String, ModelRouterTestModel, ModelRouterTestModel>>()
        val router = createRouter(controller, mockk(), mockk())
        coEvery { controller.update(any(), UnitModel, "id", mock) } returns mock
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/admin/modelroutertestmodels/id") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "id" to "id",
                    "string" to "string"
                ).formUrlEncode()
            )
        }
        assertEquals(HttpStatusCode.Found, response.status)
    }

}
