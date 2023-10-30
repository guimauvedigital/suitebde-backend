package me.nathanfallet.suitebde.plugins

import com.github.aymanizz.ktori18n.locale
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.configureStatusPage() {
    install(StatusPages) {
        status(
            HttpStatusCode.NotFound, HttpStatusCode.InternalServerError
        ) { call, status ->
            if (call.request.path().startsWith("/api/")) return@status
            call.response.status(status)
            call.respond(
                FreeMarkerContent(
                    "root/error.ftl",
                    mapOf(
                        "locale" to call.locale,
                        "code" to status.value,
                        "error" to status.description
                    )
                )
            )
        }
    }
}
