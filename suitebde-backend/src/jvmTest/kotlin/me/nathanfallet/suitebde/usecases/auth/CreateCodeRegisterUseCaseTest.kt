package me.nathanfallet.suitebde.usecases.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.models.application.Email
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.auth.RegisterPayload
import me.nathanfallet.suitebde.usecases.associations.ICreateCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.usecases.emails.ISendEmailUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateCodeRegisterUseCaseTest {

    @Test
    fun testRegister() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val sendEmailUseCase = mockk<ISendEmailUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = CreateCodeRegisterUseCase(
            getAssociationForCallUseCase, createCodeInEmailUseCase,
            sendEmailUseCase, translateUseCase, getLocaleForCallUseCase
        )
        val now = Clock.System.now()
        coEvery { getAssociationForCallUseCase(call) } returns Association(
            "id", "name", "school", "city",
            false, now, now
        )
        coEvery { createCodeInEmailUseCase("email", "id") } returns CodeInEmail(
            "email", "code", "id", Clock.System.now()
        )
        coEvery { sendEmailUseCase(any(), any()) } returns Unit
        every {
            translateUseCase(
                Locale.ENGLISH,
                any()
            )
        } answers { "t:${secondArg<String>()}" }
        every { getLocaleForCallUseCase(call) } returns Locale.ENGLISH
        every {
            translateUseCase(
                Locale.ENGLISH,
                any(),
                any()
            )
        } answers { "t:${secondArg<String>()}:${thirdArg<List<String>>()}" }
        assertEquals("code", useCase(call, RegisterPayload("email")))
        coVerify {
            sendEmailUseCase(
                Email(
                    "t:auth_register_email_title",
                    "t:auth_register_email_body:[code]"
                ),
                listOf("email")
            )
        }
    }

    @Test
    fun testRegisterNoAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = CreateCodeRegisterUseCase(
            getAssociationForCallUseCase, mockk(),
            mockk(), mockk(), mockk()
        )
        coEvery { getAssociationForCallUseCase(call) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            useCase(call, RegisterPayload("email"))
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("auth_register_no_association", exception.key)
    }

    @Test
    fun testRegisterEmailTaken() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val useCase = CreateCodeRegisterUseCase(
            getAssociationForCallUseCase, createCodeInEmailUseCase,
            mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getAssociationForCallUseCase(call) } returns Association(
            "id", "name", "school", "city",
            false, now, now
        )
        coEvery { createCodeInEmailUseCase("email", "id") } returns null
        val exception = assertFailsWith(ControllerException::class) {
            useCase(call, RegisterPayload("email"))
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals("auth_register_email_taken", exception.key)
    }

}
