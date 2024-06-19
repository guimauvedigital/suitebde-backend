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
        every { service.generateJWT(UUID(), UUID(), "access") } returns "accessToken"
        every { service.generateJWT(UUID(), UUID(), "refresh") } returns "refreshToken"
        assertEquals(
            AuthToken(
                "accessToken",
                "refreshToken",
                "associationId/uid"
            ),
            userCase(
                ClientForUser(
                    Client(UUID(), UUID(), "name", "description", "secret", "redirect"),
                    User(
                        UUID(), UUID(), "email", "password",
                        "firstName", "lastName", false, Clock.System.now()
                    )
                )
            )
        )
    }

}
