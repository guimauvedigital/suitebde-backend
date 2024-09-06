package com.suitebde.controllers.auth

import com.suitebde.models.application.Client
import com.suitebde.models.application.Email
import com.suitebde.models.associations.Association
import com.suitebde.models.associations.CreateAssociationPayload
import com.suitebde.models.auth.*
import com.suitebde.models.users.ResetInUser
import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import com.suitebde.usecases.associations.*
import com.suitebde.usecases.auth.*
import com.suitebde.usecases.users.IGetUserUseCase
import com.suitebde.usecases.users.IUpdateUserLastLoginUseCase
import dev.kaccelero.commons.emails.ISendEmailUseCase
import dev.kaccelero.commons.exceptions.ControllerException
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.commons.localization.ITranslateUseCase
import dev.kaccelero.commons.repositories.ICreateModelSuspendUseCase
import dev.kaccelero.commons.repositories.IGetModelSuspendUseCase
import dev.kaccelero.commons.repositories.IUpdateChildModelSuspendUseCase
import dev.kaccelero.commons.responses.RedirectResponse
import dev.kaccelero.commons.users.IRequireUserForCallUseCase
import dev.kaccelero.models.UUID
import io.ktor.http.*
import io.ktor.server.application.*

class AuthController(
    private val listAssociationsUseCase: IGetAssociationsUseCase,
    private val getAssociationUseCase: IGetModelSuspendUseCase<Association, UUID>,
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    private val loginUseCase: ILoginUseCase,
    private val registerUseCase: IRegisterUseCase,
    private val setSessionForCallUseCase: ISetSessionForCallUseCase,
    private val clearSessionForCallUseCase: IClearSessionForCallUseCase,
    private val requireUserForCallUseCase: IRequireUserForCallUseCase,
    private val getClientUseCase: IGetModelSuspendUseCase<Client, UUID>,
    private val getClientForUserForRefreshTokenUseCase: IGetClientForUserForRefreshTokenUseCase,
    private val getAuthCodeUseCase: IGetAuthCodeUseCase,
    private val createAuthCodeUseCase: ICreateAuthCodeUseCase,
    private val deleteAuthCodeUseCase: IDeleteAuthCodeUseCase,
    private val generateAuthTokenUseCase: IGenerateAuthTokenUseCase,
    private val getUserUseCase: IGetUserUseCase,
    private val updateUserUseCase: IUpdateChildModelSuspendUseCase<User, UUID, UpdateUserPayload, UUID>,
    private val updateUserLastLoginUseCase: IUpdateUserLastLoginUseCase,
    private val createCodeInEmailUseCase: ICreateCodeInEmailUseCase,
    private val getCodeInEmailUseCase: IGetCodeInEmailUseCase,
    private val deleteCodeInEmailUseCase: IDeleteCodeInEmailUseCase,
    private val createAssociationUseCase: ICreateModelSuspendUseCase<Association, CreateAssociationPayload>,
    private val createResetPasswordUseCase: ICreateResetPasswordUseCase,
    private val getResetPasswordUseCase: IGetResetPasswordUseCase,
    private val deleteResetPasswordUseCase: IDeleteResetPasswordUseCase,
    private val sendEmailUseCase: ISendEmailUseCase,
    private val getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    private val translateUseCase: ITranslateUseCase,
) : IAuthController {

    override fun login() {}

    override suspend fun login(call: ApplicationCall, payload: LoginPayload, redirect: String?): RedirectResponse {
        val user = loginUseCase(payload) ?: throw ControllerException(
            HttpStatusCode.Unauthorized, "auth_invalid_credentials"
        )
        setSessionForCallUseCase(call, SessionPayload(user.id))
        return RedirectResponse(redirect ?: "/users/${user.id}")
    }

    override suspend fun logout(call: ApplicationCall, redirect: String?): RedirectResponse {
        clearSessionForCallUseCase(call)
        return RedirectResponse(redirect ?: "/")
    }

    override suspend fun profile(call: ApplicationCall): RedirectResponse {
        val user = requireUserForCallUseCase(call) as User
        return RedirectResponse("/users/${user.id}")
    }

    override suspend fun associations(): List<Association> {
        return listAssociationsUseCase(true)
    }

    override suspend fun register(call: ApplicationCall, associationId: UUID?): Association {
        return getAssociationForCallUseCase(call)
            ?: associationId?.let { getAssociationUseCase(associationId) }
            ?: throw RedirectResponse("associations")
    }

    override suspend fun register(
        call: ApplicationCall,
        payload: RegisterPayload,
        associationId: UUID?,
    ): Map<String, Any> {
        val association = register(call, associationId)
        val code = createCodeInEmailUseCase(payload.email, association.id) ?: return mapOf(
            "error" to "auth_register_email_taken"
        )
        val locale = getLocaleForCallUseCase(call)
        sendEmailUseCase(
            Email(
                translateUseCase(locale, "auth_register_email_title"),
                translateUseCase(
                    locale,
                    "auth_register_email_body",
                    listOf("https://suitebde.com/auth/register/${code.code}")
                )
            ),
            listOf(payload.email)
        )
        return mapOf("success" to "auth_register_code_created")
    }

    override suspend fun registerCode(call: ApplicationCall, code: String): RegisterPayload {
        val association = getAssociationForCallUseCase(call)
        val codeInEmail = getCodeInEmailUseCase(code)?.takeIf {
            association == null || it.associationId == association.id
        } ?: throw ControllerException(HttpStatusCode.NotFound, "auth_code_invalid")
        return RegisterPayload(codeInEmail.email)
    }

    override suspend fun registerCode(
        call: ApplicationCall,
        code: String,
        payload: RegisterCodePayload,
        redirect: String?,
    ): RedirectResponse {
        val user = registerUseCase(code, payload) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
        setSessionForCallUseCase(call, SessionPayload(user.id))
        deleteCodeInEmailUseCase(code)
        return RedirectResponse(redirect ?: "/users/${user.id}")
    }

    override suspend fun authorize(call: ApplicationCall, clientId: UUID?): ClientForUser {
        val user = requireUserForCallUseCase(call)
        val client = clientId?.let { getClientUseCase(it) } ?: throw ControllerException(
            HttpStatusCode.BadRequest, "auth_invalid_client"
        )
        return ClientForUser(client, user as User)
    }

    override suspend fun authorizeRedirect(call: ApplicationCall, clientId: UUID?): Map<String, Any> {
        val client = authorize(call, clientId)
        val code = createAuthCodeUseCase(client) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
        return mapOf(
            "redirect" to client.client.redirectUri.replace("{code}", code)
        )
    }

    override fun join() {}

    override suspend fun join(call: ApplicationCall, payload: JoinPayload): Map<String, Any> {
        val code = createCodeInEmailUseCase(payload.email, null) ?: throw ControllerException(
            HttpStatusCode.BadRequest, "auth_join_email_taken"
        )
        val locale = getLocaleForCallUseCase(call)
        sendEmailUseCase(
            Email(
                translateUseCase(locale, "auth_join_email_title"),
                translateUseCase(
                    locale,
                    "auth_join_email_body",
                    listOf("https://suitebde.com/auth/join/${code.code}")
                )
            ),
            listOf(payload.email)
        )
        return mapOf("success" to "auth_join_email_sent")
    }

    override suspend fun joinCode(code: String): JoinPayload {
        return getCodeInEmailUseCase(code)?.let {
            JoinPayload(it.email)
        } ?: throw ControllerException(HttpStatusCode.NotFound, "auth_code_invalid")
    }

    override suspend fun joinCode(code: String, payload: JoinCodePayload): Map<String, Any> {
        val originalPayload = joinCode(code)
        createAssociationUseCase(
            CreateAssociationPayload(
                name = payload.name,
                school = payload.school,
                city = payload.city,
                email = originalPayload.email,
                password = payload.password,
                firstName = payload.firstName,
                lastName = payload.lastName
            )
        ) ?: throw ControllerException(HttpStatusCode.InternalServerError, "error_internal")
        deleteCodeInEmailUseCase(code)
        return mapOf("success" to "auth_join_submitted")
    }

    override suspend fun reset() {}

    override suspend fun reset(call: ApplicationCall, payload: RegisterPayload): Map<String, Any> {
        val code = createResetPasswordUseCase(payload.email) ?: return mapOf(
            "error" to "auth_reset_email_unknown"
        )
        val locale = getLocaleForCallUseCase(call)
        sendEmailUseCase(
            Email(
                translateUseCase(locale, "auth_reset_email_title"),
                translateUseCase(
                    locale,
                    "auth_reset_email_body",
                    listOf("https://suitebde.com/auth/reset/${code.code}")
                )
            ),
            listOf(payload.email)
        )
        return mapOf("success" to "auth_reset_email_sent")
    }

    override suspend fun resetCode(call: ApplicationCall, code: String): ResetInUser {
        return getResetPasswordUseCase(code) ?: throw ControllerException(
            HttpStatusCode.NotFound, "auth_code_invalid"
        )
    }

    override suspend fun resetCode(
        call: ApplicationCall,
        code: String,
        payload: ResetPasswordPayload,
    ): RedirectResponse {
        val resetCode = getResetPasswordUseCase(code) ?: throw ControllerException(
            HttpStatusCode.NotFound, "auth_code_invalid"
        )
        val user = getUserUseCase(resetCode.userId) ?: throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
        updateUserUseCase(user.id, UpdateUserPayload(password = payload.password), user.associationId)
        deleteResetPasswordUseCase(code)
        val locale = getLocaleForCallUseCase(call)
        sendEmailUseCase(
            Email(
                translateUseCase(locale, "auth_reset_email_done_title"),
                translateUseCase(locale, "auth_reset_email_done_body")
            ),
            listOf(user.email)
        )
        return RedirectResponse("/auth/login")
    }

    override suspend fun token(payload: AuthRequest): AuthToken {
        val client = getAuthCodeUseCase(payload.code)?.takeIf {
            it.client.id == payload.clientId && it.client.clientSecret == payload.clientSecret
        } ?: throw ControllerException(
            HttpStatusCode.BadRequest, "auth_invalid_code"
        )
        if (!deleteAuthCodeUseCase(payload.code)) throw ControllerException(
            HttpStatusCode.InternalServerError, "error_internal"
        )
        updateUserLastLoginUseCase(client.user.id)
        return generateAuthTokenUseCase(client)
    }

    override suspend fun refreshToken(payload: RefreshTokenPayload): AuthToken {
        val client = getClientForUserForRefreshTokenUseCase(payload.refreshToken) ?: throw ControllerException(
            HttpStatusCode.BadRequest, "auth_invalid_code"
        )
        updateUserLastLoginUseCase(client.user.id)
        return generateAuthTokenUseCase(client)
    }

}
