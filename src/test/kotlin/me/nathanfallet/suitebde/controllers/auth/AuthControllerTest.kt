package me.nathanfallet.suitebde.controllers.auth

import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.models.LocalizedString
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.application.ISendEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.ICreateAssociationUseCase
import me.nathanfallet.suitebde.usecases.associations.ICreateCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.auth.ILoginUseCase
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthControllerTest {

    private val user = User("id", "associationId", "email", "password", "firstname", "lastname", false)

    @Test
    fun testLogin() = runBlocking {
        val loginUseCase = mockk<ILoginUseCase>()
        coEvery { loginUseCase(LoginPayload("email", "password")) } returns user
        val controller = AuthController(loginUseCase, mockk(), mockk(), mockk(), mockk(), mockk())
        assertEquals(user, controller.login(LoginPayload("email", "password")))
    }

    @Test
    fun testJoin() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val sendEmailUseCase = mockk<ISendEmailUseCase>()
        val controller = AuthController(mockk(), createCodeInEmailUseCase, mockk(), mockk(), mockk(), sendEmailUseCase)
        coEvery { createCodeInEmailUseCase(Pair("email", null)) } returns CodeInEmail(
            "email", "code", null, Clock.System.now()
        )
        coEvery { sendEmailUseCase(any()) } returns Unit
        controller.join(JoinPayload("email"))
        coVerify {
            sendEmailUseCase(
                Triple(
                    "email",
                    LocalizedString.AUTH_JOIN_EMAIL_TITLE.value,
                    LocalizedString.AUTH_JOIN_EMAIL_BODY.value.format("code")
                )
            )
        }
    }

    @Test
    fun testJoinEmailTaken() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val controller = AuthController(mockk(), createCodeInEmailUseCase, mockk(), mockk(), mockk(), mockk())
        coEvery { createCodeInEmailUseCase(Pair("email", null)) } returns null
        val exception = assertThrows<ControllerException> {
            controller.join(JoinPayload("email"))
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals(LocalizedString.AUTH_JOIN_EMAIL_TAKEN, exception.error)
    }

    @Test
    fun testJoinCode() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(mockk(), mockk(), getCodeInEmailUseCase, mockk(), mockk(), mockk())
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", null, Clock.System.now()
        )
        assertEquals(JoinPayload("email"), controller.join("code"))
    }

    @Test
    fun testJoinCodeInvalid() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(mockk(), mockk(), getCodeInEmailUseCase, mockk(), mockk(), mockk())
        coEvery { getCodeInEmailUseCase("code") } returns null
        val exception = assertThrows<ControllerException> {
            controller.join("code")
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals(LocalizedString.AUTH_JOIN_CODE_INVALID, exception.error)
    }

    @Test
    fun testJoinCodePayload() = runBlocking {
        val createAssociationUseCase = mockk<ICreateAssociationUseCase>()
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>()
        val controller =
            AuthController(mockk(), mockk(), mockk(), deleteCodeInEmailUseCase, createAssociationUseCase, mockk())
        coEvery { createAssociationUseCase(any()) } returns Association(
            "id", "name", "school", "city",
            false, Clock.System.now(), Clock.System.now()
        )
        coEvery { deleteCodeInEmailUseCase("code") } returns Unit
        controller.join(
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
        val createAssociationUseCase = mockk<ICreateAssociationUseCase>()
        val controller = AuthController(mockk(), mockk(), mockk(), mockk(), createAssociationUseCase, mockk())
        coEvery { createAssociationUseCase(any()) } returns null
        val exception = assertThrows<ControllerException> {
            controller.join(
                JoinCodePayload(
                    "code", "email", "name", "school", "city",
                    "password", "firstname", "lastname"
                )
            )
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals(LocalizedString.ERROR_INTERNAL, exception.error)
    }

}