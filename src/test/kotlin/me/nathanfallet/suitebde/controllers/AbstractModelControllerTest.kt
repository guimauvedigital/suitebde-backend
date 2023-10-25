package me.nathanfallet.suitebde.controllers

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import me.nathanfallet.suitebde.controllers.mock.AbstractModelControllerBody
import me.nathanfallet.suitebde.controllers.mock.AbstractModelControllerTestImpl
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.plugins.Serialization
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase
import me.nathanfallet.suitebde.usecases.users.IGetUserForCallUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class AbstractModelControllerTest {

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

    @Test
    fun testAPIv1GetRoute() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1GetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf("mock getAll"), response.body())
    }

    @Test
    fun testAPIv1GetRouteNoAssociation() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns null
        val controller =
            AbstractModelControllerTestImpl(getAssociationForDomainUseCase, mockk<IGetUserForCallUseCase>())
        routing {
            controller.createAPIv1GetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAPIv1GetRouteNoUser() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1GetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(mapOf("error" to "Mock error"), response.body())
    }

    @Test
    fun testAPIv1GetIdRoute() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1GetIdRoute(this)
        }
        val response = client.get("/id")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("mock get id", response.body())
    }

    @Test
    fun testAPIv1GetIdRouteNoAssociation() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns null
        val controller =
            AbstractModelControllerTestImpl(getAssociationForDomainUseCase, mockk<IGetUserForCallUseCase>())
        routing {
            controller.createAPIv1GetIdRoute(this)
        }
        val response = client.get("/id")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAPIv1GetIdRouteNoUser() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1GetIdRoute(this)
        }
        val response = client.get("/id")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(mapOf("error" to "Mock error"), response.body())
    }

    @Test
    fun testAPIv1PostRoute() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.Json)
            setBody(AbstractModelControllerBody("mock post"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("mock post mock post", response.body())
    }

    @Test
    fun testAPIv1PostRouteNoAssociation() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns null
        val controller =
            AbstractModelControllerTestImpl(getAssociationForDomainUseCase, mockk<IGetUserForCallUseCase>())
        routing {
            controller.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.Json)
            setBody(AbstractModelControllerBody("mock post"))
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAPIv1PostRouteNoUser() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.Json)
            setBody(AbstractModelControllerBody("mock post"))
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(mapOf("error" to "Mock error"), response.body())
    }

    @Test
    fun testAPIv1PostRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1PostRoute(this)
        }
        val response = client.post("/") {
            setBody("invalid")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(mapOf("error" to "Invalid request body"), response.body())
    }

    @Test
    fun testAPIv1PutRoute() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            contentType(ContentType.Application.Json)
            setBody(AbstractModelControllerBody("mock put"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("mock put mock put", response.body())
    }

    @Test
    fun testAPIv1PutRouteNoAssociation() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns null
        val controller =
            AbstractModelControllerTestImpl(getAssociationForDomainUseCase, mockk<IGetUserForCallUseCase>())
        routing {
            controller.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            contentType(ContentType.Application.Json)
            setBody(AbstractModelControllerBody("mock put"))
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAPIv1PutRouteNoUser() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            contentType(ContentType.Application.Json)
            setBody(AbstractModelControllerBody("mock put"))
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(mapOf("error" to "Mock error"), response.body())
    }

    @Test
    fun testAPIv1PutRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1PutIdRoute(this)
        }
        val response = client.put("/id") {
            setBody("invalid")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals(mapOf("error" to "Invalid request body"), response.body())
    }

    @Test
    fun testAPIv1DeleteRoute() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns user
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1DeleteIdRoute(this)
        }
        val response = client.delete("/id")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun testAPIv1DeleteRouteNoAssociation() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns null
        val controller =
            AbstractModelControllerTestImpl(getAssociationForDomainUseCase, mockk<IGetUserForCallUseCase>())
        routing {
            controller.createAPIv1DeleteIdRoute(this)
        }
        val response = client.delete("/id")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun testAPIv1DeleteRouteNoUser() = testApplication {
        val client = installApp(this)
        val getAssociationForDomainUseCase = mockk<IGetAssociationForDomainUseCase>()
        val getUserForCallUseCase = mockk<IGetUserForCallUseCase>()
        coEvery { getAssociationForDomainUseCase("localhost") } returns association
        coEvery { getUserForCallUseCase(any()) } returns null
        val controller = AbstractModelControllerTestImpl(getAssociationForDomainUseCase, getUserForCallUseCase)
        routing {
            controller.createAPIv1DeleteIdRoute(this)
        }
        val response = client.delete("/id")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals(mapOf("error" to "Mock error"), response.body())
    }

}