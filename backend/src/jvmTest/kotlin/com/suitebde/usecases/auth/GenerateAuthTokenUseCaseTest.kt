package com.suitebde.usecases.auth

import com.suitebde.models.application.Client
import com.suitebde.models.auth.AuthToken
import com.suitebde.models.auth.ClientForUser
import com.suitebde.models.users.User
import com.suitebde.services.jwt.IJWTService
import dev.kaccelero.models.UUID
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class GenerateAuthTokenUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val service = mockk<IJWTService>()
        val userCase = GenerateAuthTokenUseCase(service)
        val associationId = UUID()
        val clientId = UUID()
        val userId = UUID()
        every { service.generateJWT(userId, clientId, "access") } returns "accessToken"
        every { service.generateJWT(userId, clientId, "refresh") } returns "refreshToken"
        assertEquals(
            AuthToken(
                "accessToken",
                "refreshToken",
                "$associationId/$userId"
            ),
            userCase(
                ClientForUser(
                    Client(clientId, associationId, "name", "description", "secret", "redirect"),
                    User(
                        userId, associationId, "email", "password",
                        "firstName", "lastName", false, Clock.System.now()
                    )
                )
            )
        )
    }

}
