package com.suitebde.usecases.auth

import com.suitebde.models.auth.SessionPayload
import dev.kaccelero.models.UUID
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ClearSessionForCallUseCaseTest {

    @Test
    fun invoke() = testApplication {
        environment {
            config = ApplicationConfig("application.test.conf")
        }
        application {
            install(Sessions) {
                cookie<SessionPayload>("session", SessionStorageMemory())
            }
        }
        routing {
            get {
                val useCase = ClearSessionForCallUseCase()
                val id = UUID()
                call.sessions.set(SessionPayload(id))
                assertEquals(SessionPayload(id), call.sessions.get<SessionPayload>())
                useCase(call)
                assertEquals(null, call.sessions.get<SessionPayload>())
            }
        }
        client.get("/")
    }

}
