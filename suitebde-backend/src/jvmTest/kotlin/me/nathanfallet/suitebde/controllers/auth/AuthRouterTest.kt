package me.nathanfallet.suitebde.controllers.auth

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
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.models.auth.RegisterPayload
import me.nathanfallet.suitebde.plugins.*
import org.jsoup.Jsoup
import java.util.*
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
            followRedirects = false
            install(ContentNegotiation) {
                json(Serialization.json)
            }
        }
    }

    @Test
    fun testGetLoginRoute() = testApplication {
        val client = installApp(this)
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(mockk(), getLocaleForCallUseCase)
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/auth/login")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_login_title")?.`is`("h1"))
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
    fun testGetRegisterRoute() = testApplication {
        val client = installApp(this)
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(mockk(), getLocaleForCallUseCase)
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/auth/register")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_register_title")?.`is`("h1"))
        assertEquals(true, document.getElementById("email")?.`is`("input"))
        assertEquals("email", document.getElementById("email")?.attr("name"))
        assertEquals("email", document.getElementById("email")?.attr("type"))
        assertEquals(true, document.getElementById("email")?.hasAttr("required"))
        assertEquals(true, document.getElementsByAttributeValue("type", "submit").first()?.`is`("button"))
    }

    @Test
    fun testGetRegisterCodeRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        coEvery { controller.register(any(), "code") } returns RegisterPayload("email@email.com")
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/auth/register/code")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_register_title")?.`is`("h1"))
        assertEquals(true, document.getElementById("email")?.`is`("input"))
        assertEquals("email", document.getElementById("email")?.attr("name"))
        assertEquals("email", document.getElementById("email")?.attr("type"))
        assertEquals("email@email.com", document.getElementById("email")?.attr("value"))
        assertEquals(true, document.getElementById("email")?.hasAttr("disabled"))
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
    fun testGetJoinRoute() = testApplication {
        val client = installApp(this)
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(mockk(), getLocaleForCallUseCase)
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/auth/join")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
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
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        coEvery { controller.join(any(), JoinPayload("email")) } returns Unit
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/join") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(listOf("email" to "email").formUrlEncode())
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals(
            "Please check your inbox to complete your registration",
            document.getElementById("alert-success")?.text()
        )
    }

    @Test
    fun testPostJoinRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(mockk(), getLocaleForCallUseCase)
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/join") {
            contentType(ContentType.Application.FormUrlEncoded)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals("Invalid body", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostJoinRouteEmailTaken() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        coEvery { controller.join(any(), JoinPayload("email")) } throws ControllerException(
            HttpStatusCode.BadRequest,
            "auth_join_email_taken"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/join") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(listOf("email" to "email").formUrlEncode())
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals("This email is already taken", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testGetJoinCodeRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        coEvery { controller.join(any(), "code") } returns JoinPayload("email@email.com")
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/auth/join/code")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
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
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        coEvery { controller.join(any(), "code") } throws ControllerException(
            HttpStatusCode.NotFound,
            "auth_code_invalid"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/auth/join/code")
        assertEquals(HttpStatusCode.NotFound, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals("Invalid code", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostJoinCodeRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        coEvery { controller.join(any(), "code") } returns JoinPayload("email")
        coEvery {
            controller.join(
                any(),
                "code",
                JoinCodePayload(
                    "code", "email", "name", "school", "city",
                    "password", "firstname", "lastname"
                )
            )
        } returns Unit
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/join/code") {
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
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals(
            "Your registration has been submitted, please wait for validation",
            document.getElementById("alert-success")?.text()
        )
    }

    @Test
    fun testPostJoinRouteCodeInvalidBody() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        coEvery { controller.join(any(), "code") } returns JoinPayload("email")
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/join/code") {
            contentType(ContentType.Application.FormUrlEncoded)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals("Invalid body", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostJoinCodeRouteNotFound() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        coEvery { controller.join(any(), "code") } throws ControllerException(
            HttpStatusCode.NotFound,
            "auth_code_invalid"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/join/code")
        assertEquals(HttpStatusCode.NotFound, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals("Invalid code", document.getElementById("alert-error")?.text())
    }

}
