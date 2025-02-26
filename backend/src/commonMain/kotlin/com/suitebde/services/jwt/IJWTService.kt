package com.suitebde.services.jwt

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.DecodedJWT
import dev.kaccelero.models.UUID
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

interface IJWTService {

    val verifier: JWTVerifier
    val authenticationFunction: AuthenticationFunction<JWTCredential>
    val challenge: JWTAuthChallengeFunction

    fun generateJWT(userId: UUID, clientId: UUID, type: String): String
    fun verifyJWT(token: String): DecodedJWT?

}
