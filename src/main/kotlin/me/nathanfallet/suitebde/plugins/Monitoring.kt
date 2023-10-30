package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.sentry.Sentry
import me.nathanfallet.ktor.sentry.KtorSentry
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    val env = environment.config.property("ktor.environment").getString().takeIf {
        it != "localhost" && it != "test"
    } ?: return
    Sentry.init {
        it.dsn = "https://924adee08965753ab548bec9d57b3700@o4506105040470016.ingest.sentry.io/4506106976862208"
        it.environment = env
        it.tracesSampleRate = when (env) {
            "production" -> 0.1
            "dev" -> 1.0
            else -> 0.0
        }
    }
    install(KtorSentry)
}
