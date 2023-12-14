package me.nathanfallet.suitebde.controllers.auth

import io.ktor.http.*
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
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.usecases.associations.ICreateCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetCodeInEmailUseCase
import me.nathanfallet.usecases.emails.ISendEmailUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthControllerTest {

    @Test
    fun testJoin() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val sendEmailUseCase = mockk<ISendEmailUseCase>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), createCodeInEmailUseCase,
            mockk(), mockk(), mockk(), sendEmailUseCase, getLocaleForCallUseCase, translateUseCase
        )
        coEvery { createCodeInEmailUseCase("email", null) } returns CodeInEmail(
            "email", "code", null, Clock.System.now()
        )
        coEvery { sendEmailUseCase(any(), any()) } returns Unit
        every { getLocaleForCallUseCase(any()) } returns Locale.ENGLISH
        every {
            translateUseCase(
                Locale.ENGLISH,
                any()
            )
        } answers { "t:${secondArg<String>()}" }
        every {
            translateUseCase(
                Locale.ENGLISH,
                any(),
                any()
            )
        } answers { "t:${secondArg<String>()}:${thirdArg<List<String>>()}" }
        controller.join(mockk(), JoinPayload("email"))
        coVerify {
            sendEmailUseCase(
                Email(
                    "t:auth_join_email_title",
                    "t:auth_join_email_body:[code]"
                ),
                listOf("email")
            )
        }
    }

    @Test
    fun testJoinEmailTaken() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), createCodeInEmailUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { createCodeInEmailUseCase("email", null) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.join(mockk(), JoinPayload("email"))
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals("auth_join_email_taken", exception.key)
    }

    @Test
    fun testJoinCode() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getCodeInEmailUseCase, mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", null, Clock.System.now()
        )
        assertEquals(JoinPayload("email"), controller.join(mockk(), "code"))
    }

    @Test
    fun testJoinCodeInvalid() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getCodeInEmailUseCase, mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getCodeInEmailUseCase("code") } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.join(mockk(), "code")
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("auth_code_invalid", exception.key)
    }

    @Test
    fun testJoinCodePayload() = runBlocking {
        val createAssociationUseCase = mockk<ICreateModelSuspendUseCase<Association, CreateAssociationPayload>>()
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), deleteCodeInEmailUseCase, createAssociationUseCase, mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { createAssociationUseCase(any()) } returns Association(
            "id", "name", "school", "city",
            false, now, now
        )
        coEvery { deleteCodeInEmailUseCase("code") } returns Unit
        controller.join(
            mockk(),
            "code",
            JoinCodePayload(
                "code", "email", "name", "school", "city",
                "password", "firstname", "lastname"
            )
        )
        coVerify {
            createAssociationUseCase(
                CreateAssociationPayload(
                    "name", "school", "city", "email",
                    "password", "firstname", "lastname"
                )
            )
            deleteCodeInEmailUseCase("code")
        }
    }

    @Test
    fun testJoinCodePayloadError() = runBlocking {
        val createAssociationUseCase = mockk<ICreateModelSuspendUseCase<Association, CreateAssociationPayload>>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), createAssociationUseCase, mockk(), mockk(), mockk()
        )
        coEvery { createAssociationUseCase(any()) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.join(
                mockk(),
                "code",
                JoinCodePayload(
                    "code", "email", "name", "school", "city",
                    "password", "firstname", "lastname"
                )
            )
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

}
