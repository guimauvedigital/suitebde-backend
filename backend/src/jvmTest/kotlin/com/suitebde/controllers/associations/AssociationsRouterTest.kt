package com.suitebde.controllers.associations

import com.suitebde.models.associations.Association
import com.suitebde.models.users.User
import com.suitebde.plugins.*
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.routers.createRoutes
import dev.kaccelero.serializers.Serialization
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
import kotlinx.datetime.Clock
import org.jsoup.Jsoup
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AssociationsRouterTest {

    private val association = Association(
        UUID(), "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        UUID(), UUID(), "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )

    private fun installApp(application: ApplicationTestBuilder): HttpClient {
        application.environment {
            config = ApplicationConfig("application.test.conf")
        }
        application.application {
            configureI18n()
            configureKoin()
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
        val controller = mockk<IAssociationsController>()
        val router = AssociationsRouter(controller, mockk(), mockk(), mockk(), mockk(), mockk())
        coEvery { controller.list(any()) } returns listOf(association)
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/api/v1/associations")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf(association), response.body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAssociationsController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = AssociationsRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            requireUserForCallUseCase,
            getAssociationForCallUseCase,
            getAdminMenuForCallUseCase,
        )
        coEvery { controller.list(any()) } returns listOf(association)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { getAssociationForCallUseCase(any()) } returns association
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/associations")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
    }

    @Test
    fun testGetByIdAdmin() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAssociationsController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = AssociationsRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            requireUserForCallUseCase,
            getAssociationForCallUseCase,
            getAdminMenuForCallUseCase,
        )
        coEvery { controller.get(any(), association.id) } returns association
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { getAssociationForCallUseCase(any()) } returns association
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/associations/${association.id}/update")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h2"))
    }

}
