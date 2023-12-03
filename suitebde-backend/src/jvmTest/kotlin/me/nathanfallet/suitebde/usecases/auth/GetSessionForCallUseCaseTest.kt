package me.nathanfallet.suitebde.usecases.auth

import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import me.nathanfallet.suitebde.models.auth.SessionPayload
import me.nathanfallet.suitebde.plugins.configureSessions
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSessionForCallUseCaseTest {

    @Test
    fun invoke() = testApplication {
        environment {
            config = ApplicationConfig("application.test.conf")
        }
        application {
            configureSessions()
        }
        routing {
            get {
                val useCase = GetSessionForCallUseCase()
                assertEquals(null, useCase(call))
                call.sessions.set(SessionPayload("id"))
                assertEquals(SessionPayload("id"), useCase(call))
            }
        }
        client.get("/")
    }

}
