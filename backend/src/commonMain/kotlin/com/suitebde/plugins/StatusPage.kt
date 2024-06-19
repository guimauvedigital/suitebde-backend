package com.suitebde.plugins

import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import org.koin.ktor.ext.inject

fun Application.configureStatusPage() {
    val getLocaleForCallUseCase by inject<IGetLocaleForCallUseCase>()

    install(StatusPages) {
        status(
            HttpStatusCode.NotFound, HttpStatusCode.InternalServerError
        ) { call, status ->
            if (call.request.path().startsWith("/api/")) return@status
            call.response.status(status)
            call.respondTemplate(
                "root/error.ftl",
                mapOf(
                    "locale" to getLocaleForCallUseCase(call),
                    "code" to status.value,
                    "error" to status.description
                )
            )
        }
    }
}
