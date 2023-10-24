package me.nathanfallet.suitebde

import io.ktor.server.application.*
import io.ktor.server.netty.*
import me.nathanfallet.suitebde.plugins.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    // Initialize plugins
    configureKoin()
    configureMonitoring()
    configureSerialization()
    configureRouting()
    configureTasks()
    configureSentry()
}
