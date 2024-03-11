package me.nathanfallet.suitebde.controllers.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.models.responses.RedirectResponse
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.models.application.Client
import me.nathanfallet.suitebde.models.application.Email
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CodeInEmail
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.auth.*
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.*
import me.nathanfallet.suitebde.usecases.auth.*
import me.nathanfallet.usecases.auth.AuthRequest
import me.nathanfallet.usecases.auth.AuthToken
import me.nathanfallet.usecases.emails.ISendEmailUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase
import me.nathanfallet.usecases.models.get.IGetModelSuspendUseCase
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthControllerTest {

    private val association = Association(
        "associationId", "name", "school", "city",
        true, Clock.System.now(), Clock.System.now()
    )
    private val user = User(
        "id", "associationId", "email", null,
        "firstname", "lastname", false
    )
    private val client = Client(
        "cid", "id", "name", "description",
        "secret", "app://redirect?code={code}"
    )

    @Test
    fun testLogin() = runBlocking {
        val loginUseCase = mockk<ILoginUseCase>()
        val setSessionForCallUseCase = mockk<ISetSessionForCallUseCase>()
        val call = mockk<ApplicationCall>()
        val loginPayload = LoginPayload("email", "password")
        val sessionPayload = SessionPayload("id")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), loginUseCase, mockk(), setSessionForCallUseCase, mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
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
        val sessionPayload = SessionPayload("id")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), loginUseCase, mockk(), setSessionForCallUseCase, mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), clearSessionForCallUseCase, mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), clearSessionForCallUseCase, mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
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
            mockk(), mockk(), mockk(), mockk(), mockk(), createCodeInEmailUseCase, mockk(), mockk(), mockk(),
            sendEmailUseCase, getLocaleForCallUseCase, translateUseCase
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase, mockk(), mockk(), mockk(),
            mockk(), mockk()
        )
        coEvery { requireAssociationForCallUseCase(call) } returns association
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", "associationId", Clock.System.now()
        )
        assertEquals(RegisterPayload("email"), controller.registerCode(call, "code"))
    }

    @Test
    fun testRegisterCodeInvalid() = runBlocking {
        val getCodeInEmailUseCase = mockk<IGetCodeInEmailUseCase>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), getCodeInEmailUseCase, mockk(), mockk(), mockk(), mockk(), mockk()
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
        val sessionPayload = SessionPayload("id")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), registerUseCase, setSessionForCallUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), deleteCodeInEmailUseCase,
            mockk(), mockk(), mockk(), mockk()
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
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
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, String>>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), requireUserForCallUseCase,
            getClientUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getClientUseCase("cid") } returns client
        assertEquals(ClientForUser(client, user), controller.authorize(call, "cid"))
    }

    @Test
    fun testAuthorizeNoClient() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, String>>()
        val call = mockk<ApplicationCall>()
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), requireUserForCallUseCase,
            getClientUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getClientUseCase("cid") } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.authorize(call, "cid")
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals("auth_invalid_client", exception.key)
    }

    @Test
    fun testAuthorizeWithClient() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, String>>()
        val createAuthCodeUseCase = mockk<ICreateAuthCodeUseCase>()
        val call = mockk<ApplicationCall>()
        val client = ClientForUser(client, user)
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), requireUserForCallUseCase,
            getClientUseCase, mockk(), createAuthCodeUseCase, mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getClientUseCase("cid") } returns client.client
        coEvery { createAuthCodeUseCase(client) } returns "code"
        assertEquals(
            mapOf("redirect" to "app://redirect?code=code"),
            controller.authorizeRedirect(call, client.client.id)
        )
    }

    @Test
    fun testAuthorizeWithClientInternalError() = runBlocking {
        val requireUserForCallUseCase = mockk<IRequireUserForCallUseCase>()
        val getClientUseCase = mockk<IGetModelSuspendUseCase<Client, String>>()
        val createAuthCodeUseCase = mockk<ICreateAuthCodeUseCase>()
        val call = mockk<ApplicationCall>()
        val client = ClientForUser(client, user)
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), requireUserForCallUseCase,
            getClientUseCase, mockk(), createAuthCodeUseCase, mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { requireUserForCallUseCase(call) } returns user
        coEvery { getClientUseCase("cid") } returns client.client
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), createCodeInEmailUseCase, mockk(),
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), createCodeInEmailUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase, mockk(), mockk(), mockk(), mockk(), mockk()
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase, mockk(), mockk(), mockk(), mockk(), mockk()
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
            mockk(), mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase, deleteCodeInEmailUseCase,
            createAssociationUseCase, mockk(), mockk(), mockk()
        )
        val now = Clock.System.now()
        coEvery { getCodeInEmailUseCase("code") } returns CodeInEmail(
            "email", "code", null, now
        )
        coEvery { createAssociationUseCase(any()) } returns Association(
            "id", "name", "school", "city",
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
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), getCodeInEmailUseCase, mockk(), createAssociationUseCase,
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
        val generateAuthTokenUseCase = mockk<IGenerateAuthTokenUseCase>()
        val client = ClientForUser(client, user)
        val payload = AuthRequest(client.client.clientId, client.client.clientSecret, "code")
        val token = AuthToken("token", "refresh")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getAuthCodeUseCase, mockk(), deleteAuthCodeUseCase, generateAuthTokenUseCase, mockk(), mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk()
        )
        coEvery { getAuthCodeUseCase("code") } returns client
        coEvery { generateAuthTokenUseCase(client) } returns token
        coEvery { deleteAuthCodeUseCase("code") } returns true
        assertEquals(token, controller.token(payload))
        coVerify { deleteAuthCodeUseCase("code") }
    }

    @Test
    fun testTokenInternalError() = runBlocking {
        val getAuthCodeUseCase = mockk<IGetAuthCodeUseCase>()
        val deleteAuthCodeUseCase = mockk<IDeleteAuthCodeUseCase>()
        val generateAuthTokenUseCase = mockk<IGenerateAuthTokenUseCase>()
        val client = ClientForUser(client, user)
        val payload = AuthRequest(client.client.clientId, client.client.clientSecret, "code")
        val token = AuthToken("token", "refresh")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getAuthCodeUseCase, mockk(), deleteAuthCodeUseCase, generateAuthTokenUseCase, mockk(),
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk()
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
        val payload = AuthRequest("otherClientId", client.client.clientSecret, "code")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getAuthCodeUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk()
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
        val payload = AuthRequest(client.client.clientId, "otherSecret", "code")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getAuthCodeUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk()
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
        val payload = AuthRequest(client.client.clientId, client.client.clientSecret, "code")
        val controller = AuthController(
            mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            getAuthCodeUseCase, mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(), mockk(),
            mockk(), mockk()
        )
        coEvery { getAuthCodeUseCase("code") } returns null
        val exception = assertFailsWith(ControllerException::class) {
            controller.token(payload)
        }
        assertEquals(HttpStatusCode.BadRequest, exception.code)
        assertEquals("auth_invalid_code", exception.key)
    }

}
