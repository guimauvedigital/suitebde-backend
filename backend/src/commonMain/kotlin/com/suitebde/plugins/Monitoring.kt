package com.suitebde.plugins

import dev.kaccelero.plugins.Health
import dev.kaccelero.plugins.KtorSentry
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(Health)

    val env = environment.config.property("ktor.environment").getString().takeIf {
        it != "localhost" && it != "test"
    } ?: return
    install(KtorSentry) {
        dsn = "https://924adee08965753ab548bec9d57b3700@o4506105040470016.ingest.sentry.io/4506106976862208"
        environment = env
        tracesSampleRate = when (env) {
            "production" -> 0.1
            "dev" -> 1.0
            else -> 0.0
        }
    }
}
