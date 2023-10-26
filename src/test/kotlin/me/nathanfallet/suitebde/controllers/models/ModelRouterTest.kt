package me.nathanfallet.suitebde.controllers.models

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.util.reflect.*
import io.mockk.coEvery
import io.mockk.mockk
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.plugins.Serialization
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelRouterTest {

    private val association = Association("id", "name")
    private val user = User("id", "username", "email", "password", "firstname", "lastname", false)

    private fun installApp(application: ApplicationTestBuilder): HttpClient {
        application.environment {
            config = ApplicationConfig("application.test.conf")
        }
        application.install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
            json(Serialization.json)
        }
        return application.createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Serialization.json)
            }
        }
    }

    private fun createRouter(
        controller: IModelController<String, ModelRouterTestBody, ModelRouterTestBody>,
        getAssociationForDomainUseCase: IGetAssociationForDomainUseCase,
        getUserForCallUseCase: IGetUserForCallUseCase
    ): ModelRouter<String, ModelRouterTestBody, ModelRouterTestBody> {
        return ModelRouter(
            controller,
            "test",
            typeInfo<String>(),
            typeInfo<List<String>>(),
            typeInfo<ModelRouterTestBody>(),
            typeInfo<ModelRouterTestBody>(),
            getAssociationForDomainUseCase,
            getUserForCallUseCase
        )
    }

    @Test
    fun testAPIv1GetRoute() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        coEvery { controller.getAll(association, user) } returns listOf("mock")
        routing {
            router.createAPIv1GetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf("mock"), response.body())
    }

    @Test
    fun testAPIv1GetRouteNoAssociation() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, mockk())
        coEvery { getAssociationForDomainUseCase("localhost") } returns null
        routing {
            router.createAPIv1GetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAPIv1GetRouteNoUser() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        coEvery { controller.getAll(association, null) } throws ControllerException(
            HttpStatusCode.Unauthorized,
            LocalizedString.ERROR_MOCK
        )
        routing {
            router.createAPIv1GetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(mapOf("error" to LocalizedString.ERROR_MOCK.value), response.body())
    }

    @Test
    fun testAPIv1GetIdRoute() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        coEvery { controller.get(association, user, "id") } returns "mock"
        routing {
            router.createAPIv1GetIdRoute(this)
        }
        val response = client.get("/id")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("mock", response.body())
    }

    @Test
    fun testAPIv1GetIdRouteNoAssociation() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, mockk())
        coEvery { getAssociationForDomainUseCase("localhost") } returns null
        routing {
            router.createAPIv1GetIdRoute(this)
        }
        val response = client.get("/id")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAPIv1GetIdRouteNoUser() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        coEvery { controller.get(association, null, "id") } throws ControllerException(
            HttpStatusCode.Unauthorized,
            LocalizedString.ERROR_MOCK
        )
        routing {
            router.createAPIv1GetIdRoute(this)
        }
        val response = client.get("/id")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(mapOf("error" to LocalizedString.ERROR_MOCK.value), response.body())
    }

    @Test
    fun testAPIv1PostRoute() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        coEvery { controller.create(association, user, ModelRouterTestBody("mock post")) } returns "mock"
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
    fun testAPIv1PostRouteNoAssociation() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, mockk())
        coEvery { getAssociationForDomainUseCase("localhost") } returns null
        routing {
            router.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.Json)
            setBody(ModelRouterTestBody("mock post"))
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAPIv1PostRouteNoUser() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        coEvery { controller.create(association, null, ModelRouterTestBody("mock post")) } throws ControllerException(
            HttpStatusCode.Unauthorized,
            LocalizedString.ERROR_MOCK
        )
        routing {
            router.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.Json)
            setBody(ModelRouterTestBody("mock post"))
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(mapOf("error" to LocalizedString.ERROR_MOCK.value), response.body())
    }

    @Test
    fun testAPIv1PostRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        routing {
            router.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            setBody("invalid")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(mapOf("error" to LocalizedString.ERROR_BODY_INVALID.value), response.body())
    }

    @Test
    fun testAPIv1PutRoute() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        coEvery { controller.update(association, user, "id", ModelRouterTestBody("mock put")) } returns "mock"
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
    fun testAPIv1PutRouteNoAssociation() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, mockk())
        coEvery { getAssociationForDomainUseCase("localhost") } returns null
        routing {
            router.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            contentType(ContentType.Application.Json)
            setBody(ModelRouterTestBody("mock put"))
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAPIv1PutRouteNoUser() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        coEvery {
            controller.update(
                association,
                null,
                "id",
                ModelRouterTestBody("mock put")
            )
        } throws ControllerException(
            HttpStatusCode.Unauthorized,
            LocalizedString.ERROR_MOCK
        )
        routing {
            router.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            contentType(ContentType.Application.Json)
            setBody(ModelRouterTestBody("mock put"))
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(mapOf("error" to LocalizedString.ERROR_MOCK.value), response.body())
    }

    @Test
    fun testAPIv1PutRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        routing {
            router.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            setBody("invalid")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(mapOf("error" to LocalizedString.ERROR_BODY_INVALID.value), response.body())
    }

    @Test
    fun testAPIv1DeleteRoute() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        coEvery { controller.delete(association, user, "id") } returns Unit
        routing {
            router.createAPIv1DeleteIdRoute(this)
        }
        val response = client.delete("/id")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun testAPIv1DeleteRouteNoAssociation() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, mockk())
        coEvery { getAssociationForDomainUseCase("localhost") } returns null
        routing {
            router.createAPIv1DeleteIdRoute(this)
        }
        val response = client.delete("/id")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAPIv1DeleteRouteNoUser() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        val controller = mockk<IModelController<String, ModelRouterTestBody, ModelRouterTestBody>>()
        val router = createRouter(controller, getAssociationForDomainUseCase, getUserForCallUseCase)
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        coEvery { controller.delete(association, null, "id") } throws ControllerException(
            HttpStatusCode.Unauthorized,
            LocalizedString.ERROR_MOCK
        )
        routing {
            router.createAPIv1DeleteIdRoute(this)
        }
        val response = client.delete("/id")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(mapOf("error" to LocalizedString.ERROR_MOCK.value), response.body())
    }

}