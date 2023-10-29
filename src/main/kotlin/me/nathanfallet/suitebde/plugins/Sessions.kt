package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import me.nathanfallet.suitebde.models.auth.SessionPayload

fun Application.configureSessions() {
    install(Sessions) {
        cookie<SessionPayload>("session", SessionStorageMemory()) {
            cookie.path = "/"
        }
    }
}