package com.suitebde.services.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.kaccelero.models.UUID
import kotlin.test.Test

class JWTServiceTest {

    @Test
    fun testGenerateJWT() {
        val service = JWTService("secret", "issuer")
        val token = service.generateJWT(UUID(), UUID(), "access")
        val verifier = JWT.require(Algorithm.HMAC256("secret"))
            .withIssuer("issuer")
            .build()
        verifier.verify(token)
    }

}
