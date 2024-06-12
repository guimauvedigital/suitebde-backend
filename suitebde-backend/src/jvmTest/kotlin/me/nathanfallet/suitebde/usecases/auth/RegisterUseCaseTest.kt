package me.nathanfallet.suitebde.usecases.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.auth.RegisterCodePayload
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetCodeInEmailUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class RegisterUseCaseTest {

    @Test
    fun testRegisterCodePayload() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val createUserUseCase = mockk<ICreateChildModelSuspendUseCase<User, CreateUserPayload, String>>()
        val useCase = RegisterUseCase(getCodeInEmailUseCase, createUserUseCase)
        val payload = CreateUserPayload(
            "email", "password",
            "firstname", "lastname", false
        )
        val user = User(
            "id", "associationId", "email", "password",
            "firstname", "lastname", false, Clock.System.now()
        )
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", "associationId", Clock.System.now()
        )
        coEvery { createUserUseCase(payload, "associationId") } returns user
        assertEquals(user, useCase("code", RegisterCodePayload("password", "firstname", "lastname")))
    }

    @Test
    fun testRegisterCodePayloadNotExists() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val useCase = RegisterUseCase(getCodeInEmailUseCase, mockk())
        coEvery { getCodeInEmailUseCase("code") } returns null
        assertEquals(null, useCase("code", RegisterCodePayload("password", "firstname", "lastname")))
    }

    @Test
    fun testRegisterCodePayloadNoAssociation() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val useCase = RegisterUseCase(getCodeInEmailUseCase, mockk())
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", null, Clock.System.now()
        )
        assertEquals(null, useCase("code", RegisterCodePayload("password", "firstname", "lastname")))
    }

}
