package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.usecases.application.IExpireUseCase
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
            expireUseCase.invoke(Clock.System.now())
        }
    }
}
