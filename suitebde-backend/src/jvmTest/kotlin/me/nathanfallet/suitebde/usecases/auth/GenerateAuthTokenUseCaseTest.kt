package me.nathanfallet.suitebde.usecases.auth

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.ktorx.models.auth.ClientForUser
import me.nathanfallet.suitebde.models.application.Client
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.services.jwt.IJWTService
import me.nathanfallet.usecases.auth.AuthToken
import kotlin.test.Test
import kotlin.test.assertEquals

class GenerateAuthTokenUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val service = mockk<IJWTService>()
        val userCase = GenerateAuthTokenUseCase(service)
        every { service.generateJWT("uid", "cid", "access") } returns "accessToken"
        every { service.generateJWT("uid", "cid", "refresh") } returns "refreshToken"
        assertEquals(
            AuthToken(
                "accessToken",
                "refreshToken",
                "uid"
            ),
            userCase(
                ClientForUser(
                    Client("cid", "oid", "name", "description", "secret", "redirect"),
                    User("uid", "associationId", "email", "firstName", "lastName", "password", false)
                )
            )
        )
    }

}
