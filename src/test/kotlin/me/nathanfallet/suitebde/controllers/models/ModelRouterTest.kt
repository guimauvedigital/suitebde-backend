package me.nathanfallet.suitebde.controllers.models

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.util.reflect.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.models.ModelKey
import me.nathanfallet.suitebde.models.models.ModelKeyType
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.plugins.*
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import org.jsoup.Jsoup
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelRouterTest {

    private val mock = ModelRouterTestModel("id", "string")

    private val modelKeys = listOf(
        ModelKey("id", ModelKeyType.ID),
        ModelKey("string", ModelKeyType.STRING)
    )

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
        controller: IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>,
        translateUseCase: ITranslateUseCase,
        getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase
    ): ModelRouter<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel> {
        return ModelRouter(
            "mock",
            typeInfo<ModelRouterTestModel>(),
            typeInfo<List<ModelRouterTestModel>>(),
            typeInfo<ModelRouterTestModel>(),
            typeInfo<ModelRouterTestModel>(),
            controller,
            translateUseCase,
            getAdminMenuForCallUseCase
        )
    }

    @Test
    fun testAPIv1GetRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val router = createRouter(controller, mockk(), mockk())
        coEvery { controller.getAll(any()) } returns listOf(mock)
        routing {
            router.createAPIv1GetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf(mock), response.body())
    }

    @Test
    fun testAPIv1GetRouteControllerException() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(controller, translateUseCase, mockk())
        coEvery { controller.getAll(any()) } throws ControllerException(
            HttpStatusCode.NotFound,
            "error_mock"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createAPIv1GetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(mapOf("error" to "t:error_mock"), response.body())
    }

    @Test
    fun testAPIv1GetIdRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val router = createRouter(controller, mockk(), mockk())
        coEvery { controller.get(any()) } returns mock
        routing {
            router.createAPIv1GetIdRoute(this)
        }
        val response = client.get("/id")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(mock, response.body())
    }

    @Test
    fun testAPIv1GetIdRouteControllerException() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(controller, translateUseCase, mockk())
        coEvery { controller.get(any()) } throws ControllerException(
            HttpStatusCode.NotFound,
            "error_mock"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createAPIv1GetIdRoute(this)
        }
        val response = client.get("/id")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(mapOf("error" to "t:error_mock"), response.body())
    }

    @Test
    fun testAPIv1PostRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val router = createRouter(controller, mockk(), mockk())
        coEvery { controller.create(any(), mock) } returns mock
        routing {
            router.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.Json)
            setBody(mock)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(mock, response.body())
    }

    @Test
    fun testAPIv1PostRouteControllerException() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(controller, translateUseCase, mockk())
        coEvery { controller.create(any(), mock) } throws ControllerException(
            HttpStatusCode.NotFound,
            "error_mock"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.Json)
            setBody(mock)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(mapOf("error" to "t:error_mock"), response.body())
    }

    @Test
    fun testAPIv1PostRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(mockk(), translateUseCase, mockk())
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            setBody("invalid")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(mapOf("error" to "t:error_body_invalid"), response.body())
    }

    @Test
    fun testAPIv1PutRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val router = createRouter(controller, mockk(), mockk())
        coEvery { controller.update(any(), mock) } returns mock
        routing {
            router.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            contentType(ContentType.Application.Json)
            setBody(mock)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(mock, response.body())
    }

    @Test
    fun testAPIv1PutRouteControllerException() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(controller, translateUseCase, mockk())
        coEvery { controller.update(any(), mock) } throws ControllerException(
            HttpStatusCode.NotFound,
            "error_mock"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            contentType(ContentType.Application.Json)
            setBody(mock)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(mapOf("error" to "t:error_mock"), response.body())
    }

    @Test
    fun testAPIv1PutRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(mockk(), translateUseCase, mockk())
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            setBody("invalid")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(mapOf("error" to "t:error_body_invalid"), response.body())
    }

    @Test
    fun testAPIv1DeleteRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val router = createRouter(controller, mockk(), mockk())
        coEvery { controller.delete(any()) } returns Unit
        routing {
            router.createAPIv1DeleteIdRoute(this)
        }
        val response = client.delete("/id")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun testAPIv1DeleteRouteControllerException() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(controller, translateUseCase, mockk())
        coEvery { controller.delete(any()) } throws ControllerException(
            HttpStatusCode.NotFound,
            "error_mock"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createAPIv1DeleteIdRoute(this)
        }
        val response = client.delete("/id")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(mapOf("error" to "t:error_mock"), response.body())
    }

    @Test
    fun testAdminGetRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = createRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { controller.getAll(any()) } returns listOf(mock)
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf(menuItem)
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        every { controller.modelKeys } returns modelKeys
        routing {
            router.createAdminGetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
        assertEquals(true, document.getElementById("th_id")?.`is`("th"))
        assertEquals(true, document.getElementById("th_string")?.`is`("th"))
    }

    @Test
    fun testAdminGetRouteUnauthorized() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = createRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf(menuItem)
        coEvery { controller.getAll(any()) } throws ControllerException(
            HttpStatusCode.Unauthorized,
            "error_mock"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createAdminGetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.Found, response.status)
    }

    @Test
    fun testAdminGetIdRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<ModelRouterTestModel, ModelRouterTestModel, ModelRouterTestModel>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = createRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { controller.get(any()) } returns mock
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf(menuItem)
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        every { controller.modelKeys } returns modelKeys
        routing {
            router.createAdminGetIdRoute(this)
        }
        val response = client.get("/id")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h6"))
        assertEquals(null, document.getElementById("id"))
        assertEquals(true, document.getElementById("string")?.`is`("input"))
    }

}