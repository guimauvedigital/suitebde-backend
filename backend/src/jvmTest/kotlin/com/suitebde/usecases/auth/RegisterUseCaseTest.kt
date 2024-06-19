package com.suitebde.usecases.auth

import com.suitebde.models.associations.CodeInEmail
import com.suitebde.models.auth.RegisterCodePayload
import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.User
import com.suitebde.usecases.associations.IGetCodeInEmailUseCase
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class RegisterUseCaseTest {

    @Test
    fun testRegisterCodePayload() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val createUserUseCase = mockk<ICreateChildModelSuspendUseCase<User, CreateUserPayload, UUID>>()
        val useCase = RegisterUseCase(getCodeInEmailUseCase, createUserUseCase)
        val payload = CreateUserPayload(
            "email", "password",
            "firstname", "lastname", false
        )
        val user = User(
            UUID(), UUID(), "email", "password",
            "firstname", "lastname", false, Clock.System.now()
        )
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", UUID(), Clock.System.now()
        )
        coEvery { createUserUseCase(payload, UUID()) } returns user
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
