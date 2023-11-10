package me.nathanfallet.suitebde.controllers.users

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
import me.nathanfallet.ktor.routers.controllers.base.IModelController
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.plugins.*
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.models.UnitModel
import org.jsoup.Jsoup
import kotlin.test.Test
import kotlin.test.assertEquals

class UsersRouterTest {

    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false
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
        val controller = mockk<IModelController<User, String, CreateUserPayload, UpdateUserPayload>>()
        val router = UsersRouter(controller, mockk(), mockk())
        coEvery { controller.getAll(any(), UnitModel) } returns listOf(user)
        routing {
            router.createRoutes(this)
        }
        assertEquals(listOf(user), client.get("/api/v1/users").body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<User, String, CreateUserPayload, UpdateUserPayload>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = UsersRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { controller.getAll(any(), UnitModel) } returns listOf(user)
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf()
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/users")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
    }

    @Test
    fun testGetByIdAdmin() = testApplication {
        val client = installApp(this)
        val controller = mockk<IModelController<User, String, CreateUserPayload, UpdateUserPayload>>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = UsersRouter(controller, translateUseCase, getAdminMenuForCallUseCase)
        coEvery { controller.get(any(), UnitModel, "id") } returns user
        coEvery { getAdminMenuForCallUseCase(any(), any()) } returns listOf()
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/admin/users/id")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h6"))
    }

}
