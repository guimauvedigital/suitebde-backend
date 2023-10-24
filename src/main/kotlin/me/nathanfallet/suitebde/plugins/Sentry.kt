package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import io.sentry.Sentry
import me.nathanfallet.ktor.sentry.KtorSentry

fun Application.configureSentry() {
    Sentry.init {
        it.dsn = "https://924adee08965753ab548bec9d57b3700@o4506105040470016.ingest.sentry.io/4506106976862208"
        it.tracesSampleRate = 1.0
    }
    install(KtorSentry)
}
