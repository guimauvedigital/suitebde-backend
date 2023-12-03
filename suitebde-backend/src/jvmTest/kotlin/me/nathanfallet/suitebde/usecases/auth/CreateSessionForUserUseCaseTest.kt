package me.nathanfallet.suitebde.usecases.auth

import me.nathanfallet.suitebde.models.auth.SessionPayload
import me.nathanfallet.suitebde.models.users.User
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateSessionForUserUseCaseTest {

    @Test
    fun testInvoke() {
        val useCase = CreateSessionForUserUseCase()
        assertEquals(
            SessionPayload("id"),
            useCase(
                User(
                    "id", "associationId", "email", "password",
                    "firstname", "lastname", false
                )
            )
        )
    }

}
