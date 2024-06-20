package com.suitebde.controllers.users

import com.suitebde.controllers.associations.AssociationForCallRouter
import com.suitebde.controllers.associations.AssociationsRouter
import com.suitebde.controllers.associations.IAssociationsController
import com.suitebde.models.associations.Association
import com.suitebde.models.roles.Permission
import com.suitebde.models.users.User
import com.suitebde.plugins.*
import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.associations.IRequireAssociationForCallUseCase
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

class UsersRouterTest {

    private val association = Association(
        UUID(), "name", "school", "city", true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        UUID(), association.id, "email", null, "firstname", "lastname", false, Clock.System.now()
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
        val associationController = mockk<IAssociationsController>()
        val controller = mockk<IUsersController>()
        val router = UsersRouter(
            controller,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            AssociationForCallRouter(mockk(), mockk()),
            AssociationsRouter(associationController, mockk(), mockk(), mockk(), mockk(), mockk()),
        )
        coEvery { associationController.get(any(), association.id) } returns association
        coEvery { controller.list(any(), association, null, null, null) } returns listOf(user)
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/api/v1/associations/${association.id}/users")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf(user), response.body())
    }

    @Test
    fun testGetPermissionsAPIv1() = testApplication {
        val client = installApp(this)
        val associationController = mockk<IAssociationsController>()
        val controller = mockk<IUsersController>()
        val router = UsersRouter(
            controller,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            AssociationForCallRouter(mockk(), mockk()),
            AssociationsRouter(associationController, mockk(), mockk(), mockk(), mockk(), mockk())
        )
        coEvery { associationController.get(any(), association.id) } returns association
        coEvery { controller.listPermissions(any(), association, user.id) } returns listOf(Permission.USERS_VIEW)
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/api/v1/associations/${association.id}/users/${user.id}/permissions")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf(Permission.USERS_VIEW), response.body())
    }

    @Test
    fun testGetAllAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IUsersController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = UsersRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            requireUserForCallUseCase,
            getAssociationForCallUseCase,
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.list(any(), association, null, null, null) } returns listOf(user)
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { getAssociationForCallUseCase(any()) } returns association
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/users")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_create")?.`is`("a"))
    }

    @Test
    fun testGetByIdAdmin() = testApplication {
        val client = installApp(this)
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val controller = mockk<IUsersController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val getAdminMenuForCallUseCase = mockk<IGetAdminMenuForCallUseCase>()
        val router = UsersRouter(
            controller,
            getLocaleForCallUseCase,
            translateUseCase,
            requireUserForCallUseCase,
            getAssociationForCallUseCase,
            getAdminMenuForCallUseCase,
            AssociationForCallRouter(requireAssociationForCallUseCase, mockk()),
            AssociationsRouter(mockk(), mockk(), mockk(), mockk(), mockk(), mockk())
        )
        coEvery { requireAssociationForCallUseCase(any()) } returns association
        coEvery { controller.get(any(), association, user.id) } returns user
        coEvery { requireUserForCallUseCase(any()) } returns user
        coEvery { getAssociationForCallUseCase(any()) } returns association
        coEvery { getAdminMenuForCallUseCase(any()) } returns listOf()
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every { translateUseCase(any(), any()) } answers { "t:${secondArg<String>()}" }
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/admin/users/${user.id}/update")
        assertEquals(HttpStatusCode.OK, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals(true, document.getElementById("admin_update")?.`is`("h2"))
    }

}
