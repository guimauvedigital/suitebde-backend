package me.nathanfallet.suitebde.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import me.nathanfallet.suitebde.models.application.SuiteBDEJson

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(SuiteBDEJson.json)
    }
}
