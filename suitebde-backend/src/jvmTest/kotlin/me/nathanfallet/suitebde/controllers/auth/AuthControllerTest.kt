package me.nathanfallet.suitebde.controllers.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.auth.*
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.application.ISendEmailUseCase
import me.nathanfallet.suitebde.usecases.application.ISetSessionForCallUseCase
import me.nathanfallet.suitebde.usecases.associations.ICreateCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.auth.ILoginUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthControllerTest {

    private val user = User("id", "associationId", "email", "password", "firstname", "lastname", false)

    @Test
    fun testLogin() = runBlocking {
        val loginUseCase = mockk<ILoginUseCase>()
        val setSessionForCallUseCase = mockk<ISetSessionForCallUseCase>()
        val call = mockk<ApplicationCall>()
        coEvery { loginUseCase(LoginPayload("email", "password")) } returns user
        every { setSessionForCallUseCase(call, SessionPayload("id")) } returns Unit
        val controller = AuthController(
            loginUseCase, setSessionForCallUseCase, mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        controller.login(LoginPayload("email", "password"), call)
        verify { setSessionForCallUseCase(call, SessionPayload("id")) }
    }

    @Test
    fun testLoginInvalidCredentials() = runBlocking {
        val loginUseCase = mockk<ILoginUseCase>()
        coEvery { loginUseCase(LoginPayload("email", "password")) } returns null
        val controller = AuthController(
            loginUseCase, mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.login(LoginPayload("email", "password"), mockk())
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals("auth_invalid_credentials", exception.key)
    }

    @Test
    fun testRegister() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val sendEmailUseCase = mockk<ISendEmailUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), getAssociationForCallUseCase, createCodeInEmailUseCase,
            mockk(), mockk(), mockk(), mockk(), sendEmailUseCase, translateUseCase
        )
        val now = Clock.System.now()
        coEvery { getAssociationForCallUseCase(call) } returns Association(
            "id", "name", "school", "city",
            false, now, now
        )
        coEvery { createCodeInEmailUseCase("email", "id", now) } returns CodeInEmail(
            "email", "code", "id", Clock.System.now()
        )
        coEvery { sendEmailUseCase(any(), any(), any()) } returns Unit
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
        controller.register(RegisterPayload("email"), now, Locale.ENGLISH, call)
        coVerify {
            sendEmailUseCase(
                "email",
                "t:auth_register_email_title",
                "t:auth_register_email_body:[code]"
            )
        }
    }

    @Test
    fun testRegisterNoAssociation() = runBlocking {
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), getAssociationForCallUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getAssociationForCallUseCase(call) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.register(RegisterPayload("email"), now, Locale.ENGLISH, call)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("auth_register_no_association", exception.key)
    }

    @Test
    fun testRegisterEmailTaken() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), getAssociationForCallUseCase, createCodeInEmailUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getAssociationForCallUseCase(call) } returns Association(
            "id", "name", "school", "city",
            false, now, now
        )
        coEvery { createCodeInEmailUseCase("email", "id", now) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.register(RegisterPayload("email"), now, Locale.ENGLISH, call)
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals("auth_register_email_taken", exception.key)
    }

    @Test
    fun testRegisterCode() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getCodeInEmailUseCase("code", now) } returns CodeInEmail(
            "email", "code", "id", now
        )
        assertEquals(RegisterWithAssociationPayload("email", "id"), controller.register("code", now))
    }

    @Test
    fun testRegisterCodeInvalid() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getCodeInEmailUseCase("code", now) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.register("code", now)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("auth_code_invalid", exception.key)
    }

    @Test
    fun testRegisterCodeInvalidAssociation() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getCodeInEmailUseCase("code", now) } returns CodeInEmail(
            "email", "code", null, now
        )
        val exception = assertFailsWith(ControllerException::class) {
            controller.register("code", now)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("auth_code_invalid", exception.key)
    }

    @Test
    fun testRegisterCodePayload() = runBlocking {
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>()
        val setSessionForCallUseCase = mockk<ISetSessionForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val createUserUseCase = mockk<ICreateChildModelSuspendUseCase<User, CreateUserPayload, String>>()
        val controller = AuthController(
            mockk(), setSessionForCallUseCase, mockk(), mockk(), mockk(),
            deleteCodeInEmailUseCase, createUserUseCase, mockk(), mockk(), mockk()
        )
        every { setSessionForCallUseCase(call, SessionPayload("id")) } returns Unit
        coEvery { createUserUseCase(any(), "associationId") } returns User(
            "id", "associationId", "email", "password",
            "firstname", "lastname", false
        )
        coEvery { deleteCodeInEmailUseCase("code") } returns Unit
        controller.register(
            RegisterCodePayload(
                "code", "email", "associationId",
                "password", "firstname", "lastname"
            ),
            call
        )
        coVerify {
            createUserUseCase(
                CreateUserPayload(
                    "email", "password",
                    "firstname", "lastname", false
                ),
                "associationId"
            )
            deleteCodeInEmailUseCase("code")
        }
        verify { setSessionForCallUseCase(call, SessionPayload("id")) }
    }

    @Test
    fun testRegisterCodePayloadError() = runBlocking {
        val createUserUseCase = mockk<ICreateChildModelSuspendUseCase<User, CreateUserPayload, String>>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            createUserUseCase, mockk(), mockk(), mockk()
        )
        coEvery { createUserUseCase(any(), "associationId") } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.register(
                RegisterCodePayload(
                    "code", "email", "associationId",
                    "password", "firstname", "lastname"
                ),
                mockk()
            )
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testJoin() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val sendEmailUseCase = mockk<ISendEmailUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), createCodeInEmailUseCase, mockk(),
            mockk(), mockk(), mockk(), sendEmailUseCase, translateUseCase
        )
        val now = Clock.System.now()
        coEvery { createCodeInEmailUseCase("email", null, now) } returns CodeInEmail(
            "email", "code", null, Clock.System.now()
        )
        coEvery { sendEmailUseCase(any(), any(), any()) } returns Unit
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
        controller.join(JoinPayload("email"), now, Locale.ENGLISH)
        coVerify {
            sendEmailUseCase(
                "email",
                "t:auth_join_email_title",
                "t:auth_join_email_body:[code]"
            )
        }
    }

    @Test
    fun testJoinEmailTaken() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), createCodeInEmailUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { createCodeInEmailUseCase("email", null, now) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.join(JoinPayload("email"), now, Locale.ENGLISH)
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals("auth_join_email_taken", exception.key)
    }

    @Test
    fun testJoinCode() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getCodeInEmailUseCase("code", now) } returns CodeInEmail(
            "email", "code", null, now
        )
        assertEquals(JoinPayload("email"), controller.join("code", now))
    }

    @Test
    fun testJoinCodeInvalid() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getCodeInEmailUseCase("code", now) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.join("code", now)
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("auth_code_invalid", exception.key)
    }

    @Test
    fun testJoinCodePayload() = runBlocking {
        val createAssociationUseCase = mockk<ICreateModelSuspendUseCase<Association, CreateAssociationPayload>>()
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), deleteCodeInEmailUseCase,
            mockk(), createAssociationUseCase, mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { createAssociationUseCase(any()) } returns Association(
            "id", "name", "school", "city",
            false, now, now
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
        val createAssociationUseCase = mockk<ICreateModelSuspendUseCase<Association, CreateAssociationPayload>>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), createAssociationUseCase, mockk(), mockk()
        )
        coEvery { createAssociationUseCase(any()) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.join(
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
