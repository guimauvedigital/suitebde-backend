package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*

fun Application.configureTasks() {
    environment.config.property("ktor.environment").getString().takeIf {
        it != "test"
    } ?: return

}
