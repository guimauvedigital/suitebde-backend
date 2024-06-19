package com.suitebde.plugins

import com.suitebde.services.jwt.IJWTService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val jwtService by inject<IJWTService>()

    authentication {
        jwt("api-v1-jwt") {
            verifier(jwtService.verifier)
            validate(jwtService.authenticationFunction)
            challenge(jwtService.challenge)
        }
    }
}
