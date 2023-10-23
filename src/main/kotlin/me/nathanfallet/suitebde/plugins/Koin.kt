package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules()
    }
}