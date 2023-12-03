package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.auth.RegisterPayload
import me.nathanfallet.suitebde.usecases.associations.IGetCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCodeRegisterUseCaseTest {

    @Test
    fun testRegisterCode() = runBlocking {
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val useCase = GetCodeRegisterUseCase(requireAssociationForCallUseCase, getCodeInEmailUseCase)
        val call = mockk<ApplicationCall>()
        coEvery { requireAssociationForCallUseCase(call) } returns Association(
            "associationId", "name", "school", "city", true,
            Clock.System.now(), Clock.System.now()
        )
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", "associationId", Clock.System.now()
        )
        assertEquals(RegisterPayload("email"), useCase(call, "code"))
    }

    @Test
    fun testRegisterCodeInvalid() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val useCase = GetCodeRegisterUseCase(mockk(), getCodeInEmailUseCase)
        coEvery { getCodeInEmailUseCase("code") } returns null
        assertEquals(null, useCase(mockk(), "code"))
    }

    @Test
    fun testRegisterCodeInvalidAssociation() = runBlocking {
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val useCase = GetCodeRegisterUseCase(requireAssociationForCallUseCase, getCodeInEmailUseCase)
        val call = mockk<ApplicationCall>()
        coEvery { requireAssociationForCallUseCase(call) } returns Association(
            "associationId", "name", "school", "city", true,
            Clock.System.now(), Clock.System.now()
        )
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", "otherAssociation", Clock.System.now()
        )
        assertEquals(null, useCase(call, "code"))
    }

}
