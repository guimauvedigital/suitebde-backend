package me.nathanfallet.suitebde.usecases.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import me.nathanfallet.suitebde.extensions.invoke
import kotlin.test.Test
import kotlin.test.assertTrue

class VerifyPasswordUseCaseTest {

    @Test
    fun invoke() {
        val useCase = VerifyPasswordUseCase()
        assertTrue(useCase("password", BCrypt.withDefaults().hashToString(12, "password".toCharArray())))
    }

}