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
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.models.application.SuiteBDEJson
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.models.auth.RegisterPayload
import me.nathanfallet.suitebde.plugins.configureI18n
import me.nathanfallet.suitebde.plugins.configureSecurity
import me.nathanfallet.suitebde.plugins.configureSerialization
import me.nathanfallet.suitebde.plugins.configureTemplating
import org.jsoup.Jsoup
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthRouterTest {

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
                json(SuiteBDEJson.json)
            }
        }
    }

    @Test
    fun testGetLoginRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        every { controller.login() } returns Unit
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
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        coEvery { controller.register(any(), null) } returns association
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
        coEvery { controller.registerCode(any(), "code") } returns RegisterPayload("email@email.com")
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
        assertEquals(true, document.getElementById("firstName")?.`is`("input"))
        assertEquals("firstName", document.getElementById("firstName")?.attr("name"))
        assertEquals("text", document.getElementById("firstName")?.attr("type"))
        assertEquals(true, document.getElementById("firstName")?.hasAttr("required"))
        assertEquals(true, document.getElementById("lastName")?.`is`("input"))
        assertEquals("lastName", document.getElementById("lastName")?.attr("name"))
        assertEquals("text", document.getElementById("lastName")?.attr("type"))
        assertEquals(true, document.getElementById("lastName")?.hasAttr("required"))
        assertEquals(true, document.getElementsByAttributeValue("type", "submit").first()?.`is`("button"))
    }

    @Test
    fun testGetJoinRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        every { controller.join() } returns Unit
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
        coEvery { controller.join(any(), JoinPayload("email")) } returns mapOf("success" to "auth_join_email_sent")
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
        coEvery { controller.joinCode("code") } returns JoinPayload("email@email.com")
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
        assertEquals(true, document.getElementById("firstName")?.`is`("input"))
        assertEquals("firstName", document.getElementById("firstName")?.attr("name"))
        assertEquals("text", document.getElementById("firstName")?.attr("type"))
        assertEquals(true, document.getElementById("firstName")?.hasAttr("required"))
        assertEquals(true, document.getElementById("lastName")?.`is`("input"))
        assertEquals("lastName", document.getElementById("lastName")?.attr("name"))
        assertEquals("text", document.getElementById("lastName")?.attr("type"))
        assertEquals(true, document.getElementById("lastName")?.hasAttr("required"))
        assertEquals(true, document.getElementsByAttributeValue("type", "submit").first()?.`is`("button"))
    }

    @Test
    fun testGetJoinCodeRouteNotFound() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase)
        coEvery { controller.joinCode("code") } throws ControllerException(
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
        coEvery { controller.joinCode("code") } returns JoinPayload("email")
        coEvery {
            controller.joinCode(
                "code",
                JoinCodePayload(
                    "name", "school", "city",
                    "password", "firstname", "lastname"
                )
            )
        } returns mapOf("success" to "auth_join_submitted")
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
                    "firstName" to "firstname",
                    "lastName" to "lastname"
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
        coEvery { controller.joinCode("code") } returns JoinPayload("email")
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

}
