package me.nathanfallet.suitebde.controllers.auth

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.plugins.*
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import org.jsoup.Jsoup
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthRouterTest {

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
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Serialization.json)
            }
        }
    }

    @Test
    fun testGetLoginRoute() = testApplication {
        val client = installApp(this)
        val router = AuthRouter(mockk<IAuthController>(), mockk())
        routing {
            router.createGetLoginRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("email")?.`is`("input"))
        assertEquals("email", document.getElementById("email")?.attr("name"))
        assertEquals("email", document.getElementById("email")?.attr("type"))
        assertEquals(true, document.getElementById("email")?.hasAttr("required"))
        assertEquals(true, document.getElementById("password")?.`is`("input"))
        assertEquals("password", document.getElementById("password")?.attr("name"))
        assertEquals("password", document.getElementById("password")?.attr("type"))
        assertEquals(true, document.getElementById("password")?.hasAttr("required"))
        assertEquals(true, document.getElementsByAttributeValue("type", "submit").first()?.`is`("button"))
    }

    @Test
    fun testPostLoginRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val router = AuthRouter(controller, mockk())
        coEvery { controller.login(LoginPayload("email", "password")) } returns User(
            "id", "association", "email", null,
            "firstname", "lastname", false
        )
        routing {
            router.createPostLoginRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "email" to "email",
                    "password" to "password"
                ).formUrlEncode()
            )
        }
        assertEquals(HttpStatusCode.OK, response.status)
        // TODO: Update this test when we have a real login page
    }

    @Test
    fun testPostLoginRouteInvalidCredentials() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, translateUseCase)
        coEvery { controller.login(LoginPayload("email", "password")) } throws ControllerException(
            HttpStatusCode.Unauthorized,
            "auth_invalid_credentials"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createPostLoginRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "email" to "email",
                    "password" to "password"
                ).formUrlEncode()
            )
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("t:auth_invalid_credentials", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testGetJoinRoute() = testApplication {
        val client = installApp(this)
        val router = AuthRouter(mockk<IAuthController>(), mockk())
        routing {
            router.createGetJoinRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("email")?.`is`("input"))
        assertEquals("email", document.getElementById("email")?.attr("name"))
        assertEquals("email", document.getElementById("email")?.attr("type"))
        assertEquals(true, document.getElementById("email")?.hasAttr("required"))
        assertEquals(true, document.getElementsByAttributeValue("type", "submit").first()?.`is`("button"))
    }

    @Test
    fun testPostJoinRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, translateUseCase)
        coEvery { controller.join(JoinPayload("email"), any(), any()) } returns Unit
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createPostJoinRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(listOf("email" to "email").formUrlEncode())
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("t:auth_join_email_sent", document.getElementById("alert-success")?.text())
    }

    @Test
    fun testPostJoinRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(mockk<IAuthController>(), translateUseCase)
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createPostJoinRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.FormUrlEncoded)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("t:error_body_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostJoinRouteEmailTaken() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, translateUseCase)
        coEvery { controller.join(JoinPayload("email"), any(), any()) } throws ControllerException(
            HttpStatusCode.BadRequest,
            "auth_join_email_taken"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createPostJoinRoute(this)
        }
        val response = client.post("/") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(listOf("email" to "email").formUrlEncode())
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("t:auth_join_email_taken", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testGetJoinCodeRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val router = AuthRouter(controller, mockk())
        coEvery { controller.join("code", any()) } returns JoinPayload("email@email.com")
        routing {
            router.createGetJoinCodeRoute(this)
        }
        val response = client.get("/code")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("email")?.`is`("input"))
        assertEquals("email", document.getElementById("email")?.attr("name"))
        assertEquals("email", document.getElementById("email")?.attr("type"))
        assertEquals("email@email.com", document.getElementById("email")?.attr("value"))
        assertEquals(true, document.getElementById("email")?.hasAttr("disabled"))
        assertEquals(true, document.getElementById("name")?.`is`("input"))
        assertEquals("name", document.getElementById("name")?.attr("name"))
        assertEquals("text", document.getElementById("name")?.attr("type"))
        assertEquals(true, document.getElementById("name")?.hasAttr("required"))
        assertEquals(true, document.getElementById("school")?.`is`("input"))
        assertEquals("school", document.getElementById("school")?.attr("name"))
        assertEquals("text", document.getElementById("school")?.attr("type"))
        assertEquals(true, document.getElementById("school")?.hasAttr("required"))
        assertEquals(true, document.getElementById("city")?.`is`("input"))
        assertEquals("city", document.getElementById("city")?.attr("name"))
        assertEquals("text", document.getElementById("city")?.attr("type"))
        assertEquals(true, document.getElementById("city")?.hasAttr("required"))
        assertEquals(true, document.getElementById("password")?.`is`("input"))
        assertEquals("password", document.getElementById("password")?.attr("name"))
        assertEquals("password", document.getElementById("password")?.attr("type"))
        assertEquals(true, document.getElementById("password")?.hasAttr("required"))
        assertEquals(true, document.getElementById("first_name")?.`is`("input"))
        assertEquals("first_name", document.getElementById("first_name")?.attr("name"))
        assertEquals("text", document.getElementById("first_name")?.attr("type"))
        assertEquals(true, document.getElementById("first_name")?.hasAttr("required"))
        assertEquals(true, document.getElementById("last_name")?.`is`("input"))
        assertEquals("last_name", document.getElementById("last_name")?.attr("name"))
        assertEquals("text", document.getElementById("last_name")?.attr("type"))
        assertEquals(true, document.getElementById("last_name")?.hasAttr("required"))
        assertEquals(true, document.getElementsByAttributeValue("type", "submit").first()?.`is`("button"))
    }

    @Test
    fun testGetJoinCodeRouteNotFound() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, translateUseCase)
        coEvery { controller.join("code", any()) } throws ControllerException(
            HttpStatusCode.NotFound,
            "auth_join_code_invalid"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createGetJoinCodeRoute(this)
        }
        val response = client.get("/code")
        assertEquals(HttpStatusCode.NotFound, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("t:auth_join_code_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostJoinCodeRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, translateUseCase)
        coEvery { controller.join("code", any()) } returns JoinPayload("email")
        coEvery {
            controller.join(
                JoinCodePayload(
                    "code", "email", "name", "school", "city",
                    "password", "firstname", "lastname"
                ),
                any()
            )
        } returns Unit
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createPostJoinCodeRoute(this)
        }
        val response = client.post("/code") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "name" to "name",
                    "school" to "school",
                    "city" to "city",
                    "password" to "password",
                    "first_name" to "firstname",
                    "last_name" to "lastname"
                ).formUrlEncode()
            )
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("t:auth_join_submitted", document.getElementById("alert-success")?.text())
    }

    @Test
    fun testPostJoinRouteCodeInvalidBody() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, translateUseCase)
        coEvery { controller.join("code", any()) } returns JoinPayload("email")
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createPostJoinCodeRoute(this)
        }
        val response = client.post("/code") {
            contentType(ContentType.Application.FormUrlEncoded)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("t:error_body_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostJoinCodeRouteNotFound() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, translateUseCase)
        coEvery { controller.join("code", any()) } throws ControllerException(
            HttpStatusCode.NotFound,
            "auth_join_code_invalid"
        )
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createPostJoinCodeRoute(this)
        }
        val response = client.post("/code")
        assertEquals(HttpStatusCode.NotFound, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("t:auth_join_code_invalid", document.getElementById("alert-error")?.text())
    }

}