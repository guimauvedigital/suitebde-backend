package com.suitebde.plugins

import com.suitebde.usecases.application.IExpireUseCase
import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.ktor.ext.inject
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

fun Application.configureTasks() {
    environment.config.property("ktor.environment").getString().takeIf {
        it != "test"
    } ?: return

    val expireUseCase by inject<IExpireUseCase>()

    Timer().scheduleAtFixedRate(0, 60 * 60 * 1000L) {
        CoroutineScope(Job()).launch {
            expireUseCase(Clock.System.now())
        }
    }
}
