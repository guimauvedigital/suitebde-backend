package me.nathanfallet.suitebde.controllers.associations

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.plugins.Serialization
import me.nathanfallet.suitebde.plugins.configureSerialization
import kotlin.test.Test
import kotlin.test.assertEquals

class AssociationRouterTest {

    private val association = Association(
        "id", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )

    private fun installApp(application: ApplicationTestBuilder): HttpClient {
        application.environment {
            config = ApplicationConfig("application.test.conf")
        }
        application.application {
            configureSerialization()
        }
        return application.createClient {
            install(ContentNegotiation) {
                json(Serialization.json)
            }
        }
    }

    @Test
    fun testAPIv1GetRoute() = testApplication {
        val client = installApp(this)
        val controller = mockk<IAssociationController>()
        val router = AssociationRouter(controller)
        coEvery { controller.getAll() } returns listOf(association)
        routing {
            router.createAPIv1GetRoute(this)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf(association), response.body())
    }

}