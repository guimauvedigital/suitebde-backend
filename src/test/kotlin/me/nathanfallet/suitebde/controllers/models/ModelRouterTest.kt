package me.nathanfallet.suitebde.controllers.models

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.util.reflect.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.plugins.Serialization
import me.nathanfallet.suitebde.plugins.configureI18n
import me.nathanfallet.suitebde.plugins.configureSerialization
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelRouterTest {

    private fun installApp(application: ApplicationTestBuilder): HttpClient {
        application.environment {
            config = ApplicationConfig("application.test.conf")
        }
        application.application {
            configureI18n()
            configureSerialization()
        }
        return application.createClient {
            install(ContentNegotiation) {
                json(Serialization.json)
            }
        }
    }

    private fun createRouter(
        controller: IModelController<String, ModelRouterTestBody, ModelRouterTestBody>,
        translateUseCase: ITranslateUseCase
    ): ModelRouter<String, ModelRouterTestBody, ModelRouterTestBody> {
        return ModelRouter(
            "test",
            typeInfo<String>(),
            typeInfo<List<String>>(),
            typeInfo<ModelRouterTestBody>(),
            typeInfo<ModelRouterTestBody>(),
            controller,
            translateUseCase
        )
    }

    @Test
    fun testAPIv1GetRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, mockk())
        coEvery { controller.getAll(any()) } returns listOf("mock")
        routing {
            router.createAPIv1GetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf("mock"), response.body())
    }

    @Test
    fun testAPIv1GetRouteControllerException() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(controller, translateUseCase)
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
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, mockk())
        coEvery { controller.get(any()) } returns "mock"
        routing {
            router.createAPIv1GetIdRoute(this)
        }
        val response = client.get("/id")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("mock", response.body())
    }

    @Test
    fun testAPIv1GetIdRouteControllerException() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(controller, translateUseCase)
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
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, mockk())
        coEvery { controller.create(any(), ModelRouterTestBody("mock post")) } returns "mock"
        routing {
            router.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.Json)
            setBody(ModelRouterTestBody("mock post"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("mock", response.body())
    }

    @Test
    fun testAPIv1PostRouteControllerException() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(controller, translateUseCase)
        coEvery { controller.create(any(), ModelRouterTestBody("mock post")) } throws ControllerException(
            HttpStatusCode.NotFound,
            "error_mock"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.Json)
            setBody(ModelRouterTestBody("mock post"))
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(mapOf("error" to "t:error_mock"), response.body())
    }

    @Test
    fun testAPIv1PostRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(mockk(), translateUseCase)
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
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, mockk())
        coEvery { controller.update(any(), ModelRouterTestBody("mock put")) } returns "mock"
        routing {
            router.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            contentType(ContentType.Application.Json)
            setBody(ModelRouterTestBody("mock put"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("mock", response.body())
    }

    @Test
    fun testAPIv1PutRouteControllerException() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(controller, translateUseCase)
        coEvery { controller.update(any(), ModelRouterTestBody("mock put")) } throws ControllerException(
            HttpStatusCode.NotFound,
            "error_mock"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            contentType(ContentType.Application.Json)
            setBody(ModelRouterTestBody("mock put"))
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(mapOf("error" to "t:error_mock"), response.body())
    }

    @Test
    fun testAPIv1PutRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(mockk(), translateUseCase)
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
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, mockk())
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
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = createRouter(controller, translateUseCase)
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

}