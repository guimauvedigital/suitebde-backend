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
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.models.auth.*
import me.nathanfallet.suitebde.plugins.*
import me.nathanfallet.usecases.localization.ITranslateUseCase
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
        val router = AuthRouter(mockk<IAuthController>(), getLocaleForCallUseCase, mockk())
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
    fun testPostLoginRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val router = AuthRouter(controller, mockk(), mockk())
        coEvery { controller.login(LoginPayload("email", "password"), any()) } returns Unit
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/login") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "email" to "email",
                    "password" to "password"
                ).formUrlEncode()
            )
        }
        assertEquals(HttpStatusCode.Found, response.status)
        coVerify { controller.login(LoginPayload("email", "password"), any()) }
    }

    @Test
    fun testPostLoginRouteInvalidCredentials() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.login(LoginPayload("email", "password"), any()) } throws ControllerException(
            HttpStatusCode.Unauthorized,
            "auth_invalid_credentials"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/login") {
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
        assertEquals(true, document.getElementById("auth_login_title")?.`is`("h1"))
        assertEquals("t:auth_invalid_credentials", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostLoginRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(mockk(), getLocaleForCallUseCase, translateUseCase)
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/login") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "email" to "email"
                ).formUrlEncode()
            )
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_login_title")?.`is`("h1"))
        assertEquals("t:error_body_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testGetRegisterRoute() = testApplication {
        val client = installApp(this)
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(mockk<IAuthController>(), getLocaleForCallUseCase, mockk())
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
    fun testPostRegisterRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.register(RegisterPayload("email"), any(), any(), any()) } returns Unit
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/register") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(listOf("email" to "email").formUrlEncode())
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_register_title")?.`is`("h1"))
        assertEquals("t:auth_register_email_sent", document.getElementById("alert-success")?.text())
    }

    @Test
    fun testPostRegisterRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(mockk<IAuthController>(), getLocaleForCallUseCase, translateUseCase)
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/register") {
            contentType(ContentType.Application.FormUrlEncoded)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_register_title")?.`is`("h1"))
        assertEquals("t:error_body_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostRegisterRouteEmailTaken() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.register(RegisterPayload("email"), any(), any(), any()) } throws ControllerException(
            HttpStatusCode.BadRequest,
            "auth_register_email_taken"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/register") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(listOf("email" to "email").formUrlEncode())
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_register_title")?.`is`("h1"))
        assertEquals("t:auth_register_email_taken", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testGetRegisterCodeRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, mockk())
        coEvery { controller.register("code", any()) } returns RegisterWithAssociationPayload("email@email.com", "id")
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
    fun testGetRegisterCodeRouteNotFound() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.register("code", any()) } throws ControllerException(
            HttpStatusCode.NotFound,
            "auth_code_invalid"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/auth/register/code")
        assertEquals(HttpStatusCode.NotFound, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_register_title")?.`is`("h1"))
        assertEquals("t:auth_code_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostRegisterCodeRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, mockk(), translateUseCase)
        coEvery { controller.register("code", any()) } returns RegisterWithAssociationPayload(
            "email", "associationId"
        )
        coEvery {
            controller.register(
                RegisterCodePayload(
                    "code", "email", "associationId",
                    "password", "firstname", "lastname"
                ),
                any()
            )
        } returns Unit
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/register/code") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "password" to "password",
                    "first_name" to "firstname",
                    "last_name" to "lastname"
                ).formUrlEncode()
            )
        }
        assertEquals(HttpStatusCode.Found, response.status)
        coVerify {
            controller.register(
                RegisterCodePayload(
                    "code", "email", "associationId",
                    "password", "firstname", "lastname"
                ),
                any()
            )
        }
    }

    @Test
    fun testPostRegisterCodeRouteNotFound() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.register("code", any()) } throws ControllerException(
            HttpStatusCode.NotFound,
            "auth_code_invalid"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/register/code")
        assertEquals(HttpStatusCode.NotFound, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_register_title")?.`is`("h1"))
        assertEquals("t:auth_code_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostRegisterCodeRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.register("code", any()) } returns RegisterWithAssociationPayload(
            "email", "associationId"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/register/code") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "password" to "password",
                ).formUrlEncode()
            )
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_register_title")?.`is`("h1"))
        assertEquals("t:error_body_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testGetJoinRoute() = testApplication {
        val client = installApp(this)
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(mockk<IAuthController>(), getLocaleForCallUseCase, mockk())
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
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.join(JoinPayload("email"), any(), any()) } returns Unit
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
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
        assertEquals("t:auth_join_email_sent", document.getElementById("alert-success")?.text())
    }

    @Test
    fun testPostJoinRouteInvalidBody() = testApplication {
        val client = installApp(this)
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(mockk<IAuthController>(), getLocaleForCallUseCase, translateUseCase)
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/join") {
            contentType(ContentType.Application.FormUrlEncoded)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals("t:error_body_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostJoinRouteEmailTaken() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.join(JoinPayload("email"), any(), any()) } throws ControllerException(
            HttpStatusCode.BadRequest,
            "auth_join_email_taken"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
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
        assertEquals("t:auth_join_email_taken", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testGetJoinCodeRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, mockk())
        coEvery { controller.join("code", any()) } returns JoinPayload("email@email.com")
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
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.join("code", any()) } throws ControllerException(
            HttpStatusCode.NotFound,
            "auth_code_invalid"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/auth/join/code")
        assertEquals(HttpStatusCode.NotFound, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals("t:auth_code_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostJoinCodeRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.join("code", any()) } returns JoinPayload("email")
        coEvery {
            controller.join(
                JoinCodePayload(
                    "code", "email", "name", "school", "city",
                    "password", "firstname", "lastname"
                )
            )
        } returns Unit
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
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
        assertEquals("t:auth_join_submitted", document.getElementById("alert-success")?.text())
    }

    @Test
    fun testPostJoinRouteCodeInvalidBody() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.join("code", any()) } returns JoinPayload("email")
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/join/code") {
            contentType(ContentType.Application.FormUrlEncoded)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals("t:error_body_invalid", document.getElementById("alert-error")?.text())
    }

    @Test
    fun testPostJoinCodeRouteNotFound() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAuthController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val router = AuthRouter(controller, getLocaleForCallUseCase, translateUseCase)
        coEvery { controller.join("code", any()) } throws ControllerException(
            HttpStatusCode.NotFound,
            "auth_code_invalid"
        )
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.post("/en/auth/join/code")
        assertEquals(HttpStatusCode.NotFound, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("auth_join_title")?.`is`("h1"))
        assertEquals("t:auth_code_invalid", document.getElementById("alert-error")?.text())
    }

}
