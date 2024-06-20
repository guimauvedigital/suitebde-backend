package com.suitebde.controllers.auth

import com.suitebde.models.application.Client
import com.suitebde.models.application.Email
import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CodeInEmail
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.auth.*
import com.suitebde.models.users.User
import com.suitebde.usecases.associations.*
import com.suitebde.usecases.auth.*
import com.suitebde.usecases.users.IUpdateUserLastLoginUseCase
import dev.kaccelero.commons.emails.ISendEmailUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.repositories.ICreateModelSuspendUseCase
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.commons.responses.RedirectResponse
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthControllerTest {

    private val association = Association(
        UUID(), "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        UUID(), association.id, "email", null,
        "firstname", "lastname", false, Clock.System.now()
    )
    private val client = Client(
        UUID(), association.id, "name", "description",
        "secret", "app://redirect?code={code}"
    )

    @Test
    fun testLogin() = runBlocking {
        val loginUseCase = mockk<ILoginUseCase>()
        val setSessionForCallUseCase = mockk<ISetSessionForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val loginPayload = LoginPayload("email", "password")
        val sessionPayload = SessionPayload(user.id)
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), loginUseCase, mockk(), setSessionForCallUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { loginUseCase(loginPayload) } returns user
        every { setSessionForCallUseCase(call, sessionPayload) } returns Unit
        assertEquals(RedirectResponse("/"), controller.login(call, loginPayload, null))
        verify { setSessionForCallUseCase(call, sessionPayload) }
    }

    @Test
    fun testLoginRedirect() = runBlocking {
        val loginUseCase = mockk<ILoginUseCase>()
        val setSessionForCallUseCase = mockk<ISetSessionForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val loginPayload = LoginPayload("email", "password")
        val sessionPayload = SessionPayload(user.id)
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), loginUseCase, mockk(), setSessionForCallUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { loginUseCase(loginPayload) } returns user
        every { setSessionForCallUseCase(call, sessionPayload) } returns Unit
        assertEquals(RedirectResponse("/redirect"), controller.login(call, loginPayload, "/redirect"))
        verify { setSessionForCallUseCase(call, sessionPayload) }
    }

    @Test
    fun testLoginInvalidCredentials() = runBlocking {
        val loginUseCase = mockk<ILoginUseCase>()
        val call = mockk<ApplicationCall>()
        val loginPayload = LoginPayload("email", "password")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), loginUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { loginUseCase(loginPayload) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.login(call, loginPayload, null)
        }
        assertEquals(HttpStatusCode.Unauthorized, exception.code)
        assertEquals("auth_invalid_credentials", exception.key)
    }

    @Test
    fun testLogout() = runBlocking {
        val clearSessionForCallUseCase = mockk<IClearSessionForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), clearSessionForCallUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        every { clearSessionForCallUseCase(call) } returns Unit
        assertEquals(RedirectResponse("/"), controller.logout(call, null))
        verify { clearSessionForCallUseCase(call) }
    }

    @Test
    fun testLogoutRedirect() = runBlocking {
        val clearSessionForCallUseCase = mockk<IClearSessionForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), clearSessionForCallUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        every { clearSessionForCallUseCase(call) } returns Unit
        assertEquals(RedirectResponse("/redirect"), controller.logout(call, "/redirect"))
        verify { clearSessionForCallUseCase(call) }
    }

    @Test
    fun testRegister() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val sendEmailUseCase = mockk<ISendEmailUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val getAssociationForCallUseCase = mockk<IGetAssociationForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val registerPayload = RegisterPayload("email")
        val controller = AuthController(
            mockk(), mockk(), getAssociationForCallUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), createCodeInEmailUseCase, mockk(), mockk(),
            mockk(), sendEmailUseCase, getLocaleForCallUseCase, translateUseCase
        )
        coEvery { getAssociationForCallUseCase(call) } returns association
        coEvery { createCodeInEmailUseCase("email", association.id) } returns CodeInEmail(
            "email", "code", association.id, Clock.System.now()
        )
        coEvery { sendEmailUseCase(any(), any()) } returns Unit
        every {
            translateUseCase(Locale.ENGLISH, any())
        } answers { "t:${secondArg<String>()}" }
        every { getLocaleForCallUseCase(call) } returns Locale.ENGLISH
        every {
            translateUseCase(Locale.ENGLISH, any(), any())
        } answers { "t:${secondArg<String>()}:${thirdArg<List<String>>()}" }
        controller.register(call, registerPayload, null)
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
    fun testRegisterCode() = runBlocking {
        val requireAssociationForCallUseCase = mockk<IRequireAssociationForCallUseCase>()
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), requireAssociationForCallUseCase, mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireAssociationForCallUseCase(call) } returns association
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", association.id, Clock.System.now()
        )
        assertEquals(RegisterPayload("email"), controller.registerCode(call, "code"))
    }

    @Test
    fun testRegisterCodeInvalid() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getCodeInEmailUseCase("code") } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.registerCode(call, "code")
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("auth_code_invalid", exception.key)
    }

    @Test
    fun testRegisterCodePayload() = runBlocking {
        val registerUseCase = mockk<IRegisterUseCase>()
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>()
        val setSessionForCallUseCase = mockk<ISetSessionForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val registerPayload = RegisterCodePayload("password", "firstname", "lastname")
        val sessionPayload = SessionPayload(user.id)
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), registerUseCase, setSessionForCallUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            deleteCodeInEmailUseCase, mockk(), mockk(), mockk(), mockk()
        )
        coEvery { registerUseCase("code", registerPayload) } returns user
        every { setSessionForCallUseCase(call, sessionPayload) } returns Unit
        coEvery { deleteCodeInEmailUseCase("code") } returns Unit
        controller.registerCode(call, "code", registerPayload, null)
        coVerify { deleteCodeInEmailUseCase("code") }
        verify { setSessionForCallUseCase(call, sessionPayload) }
    }

    @Test
    fun testRegisterCodePayloadError() = runBlocking {
        val registerUseCase = mockk<IRegisterUseCase>()
        val call = mockk<ApplicationCall>()
        val registerPayload = RegisterCodePayload("password", "firstname", "lastname")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), registerUseCase, mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { registerUseCase("code", registerPayload) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.registerCode(call, "code", registerPayload, null)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testAuthorize() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, UUID>>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), requireUserForCallUseCase,
            getClientUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getClientUseCase(client.id) } returns client
        assertEquals(ClientForUser(client, user), controller.authorize(call, client.id))
    }

    @Test
    fun testAuthorizeNoClient() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, UUID>>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), requireUserForCallUseCase,
            getClientUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getClientUseCase(client.id) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.authorize(call, client.id)
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals("auth_invalid_client", exception.key)
    }

    @Test
    fun testAuthorizeWithClient() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, UUID>>()
        val createAuthCodeUseCase = mockk<ICreateAuthCodeUseCase>()
        val call = mockk<ApplicationCall>()
        val client = ClientForUser(client, user)
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), requireUserForCallUseCase,
            getClientUseCase, mockk(), mockk(), createAuthCodeUseCase, mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getClientUseCase(client.client.id) } returns client.client
        coEvery { createAuthCodeUseCase(client) } returns "code"
        assertEquals(
            mapOf("redirect" to "app://redirect?code=code"),
            controller.authorizeRedirect(call, client.client.id)
        )
    }

    @Test
    fun testAuthorizeWithClientInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, UUID>>()
        val createAuthCodeUseCase = mockk<ICreateAuthCodeUseCase>()
        val call = mockk<ApplicationCall>()
        val client = ClientForUser(client, user)
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), requireUserForCallUseCase,
            getClientUseCase, mockk(), mockk(), createAuthCodeUseCase, mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getClientUseCase(client.client.id) } returns client.client
        coEvery { createAuthCodeUseCase(client) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.authorizeRedirect(call, client.client.id)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testJoin() = runBlocking {
        val createCodeInEmailUseCase = mockk<ICreateCodeInEmailUseCase>()
        val sendEmailUseCase = mockk<ISendEmailUseCase>()
        val getLocaleForCallUseCase = mockk<IGetLocaleForCallUseCase>()
        val translateUseCase = mockk<ITranslateUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), createCodeInEmailUseCase, mockk(),
            mockk(), mockk(), sendEmailUseCase, getLocaleForCallUseCase, translateUseCase
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), createCodeInEmailUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", null, Clock.System.now()
        )
        assertEquals(JoinPayload("email"), controller.joinCode("code"))
    }

    @Test
    fun testJoinCodeInvalid() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getCodeInEmailUseCase("code") } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.joinCode("code")
        }
        assertEquals(HttpStatusCode.NotFound, exception.code)
        assertEquals("auth_code_invalid", exception.key)
    }

    @Test
    fun testJoinCodePayload() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val createAssociationUseCase = mockk<ICreateModelSuspendUseCase<Association, CreateAssociationPayload>>()
        val deleteCodeInEmailUseCase = mockk<IDeleteCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase,
            deleteCodeInEmailUseCase, createAssociationUseCase, mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", null, now
        )
        coEvery { createAssociationUseCase(any()) } returns Association(
            UUID(), "name", "school", "city",
            false, now, now
        )
        coEvery { deleteCodeInEmailUseCase("code") } returns Unit
        controller.joinCode(
            "code",
            JoinCodePayload(
                "name", "school", "city",
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
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase, mockk(), createAssociationUseCase,
            mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", null, now
        )
        coEvery { createAssociationUseCase(any()) } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.joinCode(
                "code",
                JoinCodePayload(
                    "name", "school", "city",
                    "password", "firstname", "lastname"
                )
            )
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testToken() = runBlocking {
        val getAuthCodeUseCase = mockk<IGetAuthCodeUseCase>()
        val deleteAuthCodeUseCase = mockk<IDeleteAuthCodeUseCase>()
        val updateUserLastLoginUseCase = mockk<IUpdateUserLastLoginUseCase>()
        val generateAuthTokenUseCase = mockk<IGenerateAuthTokenUseCase>()
        val client = ClientForUser(client, user)
        val payload = AuthRequest(client.client.id, client.client.clientSecret, "code")
        val token = AuthToken("token", "refresh", "id")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getAuthCodeUseCase, mockk(), deleteAuthCodeUseCase, generateAuthTokenUseCase, updateUserLastLoginUseCase,
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getAuthCodeUseCase("code") } returns client
        coEvery { generateAuthTokenUseCase(client) } returns token
        coEvery { updateUserLastLoginUseCase(client.user.id) } returns true
        coEvery { deleteAuthCodeUseCase("code") } returns true
        assertEquals(token, controller.token(payload))
        coVerify { updateUserLastLoginUseCase(client.user.id) }
        coVerify { deleteAuthCodeUseCase("code") }
    }

    @Test
    fun testTokenInternalError() = runBlocking {
        val getAuthCodeUseCase = mockk<IGetAuthCodeUseCase>()
        val deleteAuthCodeUseCase = mockk<IDeleteAuthCodeUseCase>()
        val generateAuthTokenUseCase = mockk<IGenerateAuthTokenUseCase>()
        val client = ClientForUser(client, user)
        val payload = AuthRequest(client.client.id, client.client.clientSecret, "code")
        val token = AuthToken("token", "refresh", "id")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getAuthCodeUseCase, mockk(), deleteAuthCodeUseCase, generateAuthTokenUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getAuthCodeUseCase("code") } returns client
        coEvery { generateAuthTokenUseCase(client) } returns token
        coEvery { deleteAuthCodeUseCase("code") } returns false
        val exception = assertFailsWith(ControllerException::class) {
            controller.token(payload)
        }
        assertEquals(HttpStatusCode.InternalServerError, exception.code)
        assertEquals("error_internal", exception.key)
    }

    @Test
    fun testTokenInvalidClientId() = runBlocking {
        val getAuthCodeUseCase = mockk<IGetAuthCodeUseCase>()
        val client = ClientForUser(client, user)
        val payload = AuthRequest(UUID(), client.client.clientSecret, "code")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getAuthCodeUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk()
        )
        coEvery { getAuthCodeUseCase("code") } returns client
        val exception = assertFailsWith(ControllerException::class) {
            controller.token(payload)
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals("auth_invalid_code", exception.key)
    }

    @Test
    fun testTokenInvalidClientSecret() = runBlocking {
        val getAuthCodeUseCase = mockk<IGetAuthCodeUseCase>()
        val client = ClientForUser(client, user)
        val payload = AuthRequest(client.client.id, "otherSecret", "code")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getAuthCodeUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk()
        )
        coEvery { getAuthCodeUseCase("code") } returns client
        val exception = assertFailsWith(ControllerException::class) {
            controller.token(payload)
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals("auth_invalid_code", exception.key)
    }

    @Test
    fun testTokenInvalidCode() = runBlocking {
        val getAuthCodeUseCase = mockk<IGetAuthCodeUseCase>()
        val client = ClientForUser(client, user)
        val payload = AuthRequest(client.client.id, client.client.clientSecret, "code")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getAuthCodeUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk()
        )
        coEvery { getAuthCodeUseCase("code") } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.token(payload)
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals("auth_invalid_code", exception.key)
    }

}
