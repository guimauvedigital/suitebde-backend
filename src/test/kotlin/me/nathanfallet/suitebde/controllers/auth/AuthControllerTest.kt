package me.nathanfallet.suitebde.controllers.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.database.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.ILoginUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthControllerTest {

    private val user = User("id", "associationId", "email", "password", "firstname", "lastname", false)

    @Test
    fun testLogin() = runBlocking {
        val loginUseCase = mockk<ILoginUseCase>()
        coEvery { loginUseCase(LoginPayload("email", "associationId", "password")) } returns user
        val controller = AuthController(loginUseCase)
        assertEquals(user, controller.login(LoginPayload("email", "associationId", "password")))
    }

}