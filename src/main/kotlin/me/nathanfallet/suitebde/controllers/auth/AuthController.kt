package me.nathanfallet.suitebde.controllers.auth

import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.extensions.invoke
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.auth.SessionPayload
import me.nathanfallet.suitebde.models.exceptions.ControllerException
import me.nathanfallet.suitebde.usecases.application.ISendEmailUseCase
import me.nathanfallet.suitebde.usecases.application.ISetSessionForCallUseCase
import me.nathanfallet.suitebde.usecases.application.ITranslateUseCase
import me.nathanfallet.suitebde.usecases.associations.ICreateAssociationUseCase
import me.nathanfallet.suitebde.usecases.associations.ICreateCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.auth.ILoginUseCase
import java.util.*

class AuthController(
    private val loginUseCase: ILoginUseCase,
    private val setSessionForCallUseCase: ISetSessionForCallUseCase,
    private val createCodeInEmailUseCase: ICreateCodeInEmailUseCase,
    private val getCodeInEmailUseCase: IGetCodeInEmailUseCase,
    private val deleteCodeInEmailUseCase: IDeleteCodeInEmailUseCase,
    private val createAssociationUseCase: ICreateAssociationUseCase,
    private val sendEmailUseCase: ISendEmailUseCase,
    private val translateUseCase: ITranslateUseCase
) : IAuthController {

    override suspend fun login(payload: LoginPayload, call: ApplicationCall) {
        val user = loginUseCase(payload) ?: throw ControllerException(
            HttpStatusCode.Unauthorized,
            "auth_invalid_credentials"
        )
        setSessionForCallUseCase(call, SessionPayload(user.id))
    }

    override suspend fun join(payload: JoinPayload, joiningAt: Instant, locale: Locale) {
        val code =
            createCodeInEmailUseCase(payload.email, null, joiningAt) ?: throw ControllerException(
                HttpStatusCode.BadRequest,
                "auth_join_email_taken"
            )
        sendEmailUseCase(
                payload.email,
                translateUseCase(locale, "auth_join_email_title"),
                translateUseCase(locale, "auth_join_email_body", listOf(code.code))
        )
    }

    override suspend fun join(code: String, joiningAt: Instant): JoinPayload {
        return getCodeInEmailUseCase(code, joiningAt)?.let {
            JoinPayload(it.email)
        } ?: throw ControllerException(HttpStatusCode.NotFound, "auth_join_code_invalid")
    }

    override suspend fun join(payload: JoinCodePayload, joiningAt: Instant) {
        createAssociationUseCase(
                CreateAssociationPayload(
                    name = payload.name,
                    school = payload.school,
                    city = payload.city,
                    email = payload.email,
                    password = payload.password,
                    firstName = payload.firstName,
                    lastName = payload.lastName
                ),
                joiningAt
        ) ?: throw ControllerException(HttpStatusCode.InternalServerError, "error_internal")
        deleteCodeInEmailUseCase(payload.code)
    }

}