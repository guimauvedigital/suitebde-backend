package me.nathanfallet.suitebde.plugins

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.jsoup.Jsoup
import kotlin.test.Test
import kotlin.test.assertEquals

class RoutingTest {

    @Test
    fun testErrorPage() = testApplication {
        environment {
            config = ApplicationConfig("application.test.conf")
        }
        application {
            configureKoin()
            configureI18n()
            configureTemplating()
            configureStatusPage()
        }
        val response = client.get("/notfound")
        assertEquals(HttpStatusCode.NotFound, response.status)
        val document = Jsoup.parse(response.bodyAsText())
        assertEquals("404", document.getElementById("number")?.text())
    }

    @Test
    fun testErrorAPI() = testApplication {
        environment {
            config = ApplicationConfig("application.test.conf")
        }
        application {
            configureStatusPage()
        }
        val response = client.get("/api/notfound")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("", response.bodyAsText())
    }

}
