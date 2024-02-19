package me.nathanfallet.suitebde.controllers.root

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.plugins.configureI18n
import me.nathanfallet.suitebde.plugins.configureSecurity
import me.nathanfallet.suitebde.plugins.configureTemplating
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RootRouterTest {

    private fun installApp(application: ApplicationTestBuilder): HttpClient {
        application.environment {
            config = ApplicationConfig("application.test.conf")
        }
        application.application {
            configureI18n()
            configureSecurity()
            configureTemplating()
        }
        return application.createClient {}
    }

    @Test
    fun testGetHome() = testApplication {
        val client = installApp(this)
        val controller = mockk<IRootController>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val router = RootRouter(
            controller,
            getLocaleForCallUseCase
        )
        coEvery { controller.home() } returns Unit
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/home")
        assertEquals(HttpStatusCode.OK, response.status)
    }

}
