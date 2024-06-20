package com.suitebde.controllers.root

import com.suitebde.plugins.configureI18n
import com.suitebde.plugins.configureKoin
import com.suitebde.plugins.configureSecurity
import com.suitebde.plugins.configureTemplating
import com.suitebde.usecases.web.IGetRootMenuUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.routers.createRoutes
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
            configureKoin()
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
        val getRootMenuUseCase = mockk<IGetRootMenuUseCase>()
        val router = RootRouter(
            controller,
            getLocaleForCallUseCase,
            getRootMenuUseCase
        )
        coEvery { controller.home() } returns Unit
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        coEvery { getRootMenuUseCase(any()) } returns listOf()
        routing {
            router.createRoutes(this)
        }
        val response = client.get("/en/home")
        assertEquals(HttpStatusCode.OK, response.status)
    }

}
