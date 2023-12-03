package me.nathanfallet.suitebde.controllers.auth

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.controllers.auth.AuthWithCodeController
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.auth.*
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.models.application.Email
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.auth.*
import me.nathanfallet.suitebde.usecases.associations.ICreateCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetCodeInEmailUseCase
import me.nathanfallet.usecases.emails.ISendEmailUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase

class AuthController(
    loginUseCase: ILoginUseCase<LoginPayload>,
    registerUseCase: IRegisterUseCase<RegisterCodePayload>,
    createSessionForUserUseCase: ICreateSessionForUserUseCase,
    setSessionForCallUseCase: ISetSessionForCallUseCase,
    createCodeRegisterUseCase: ICreateCodeRegisterUseCase<RegisterPayload>,
    getCodeRegisterUseCase: IGetCodeRegisterUseCase<RegisterPayload>,
    deleteCodeRegisterUseCase: IDeleteCodeRegisterUseCase,
    private val createCodeInEmailUseCase: ICreateCodeInEmailUseCase,
    private val getCodeInEmailUseCase: IGetCodeInEmailUseCase,
    private val deleteCodeInEmailUseCase: IDeleteCodeInEmailUseCase,
    private val createAssociationUseCase: ICreateModelSuspendUseCase<Association, CreateAssociationPayload>,
    private val sendEmailUseCase: ISendEmailUseCase,
    private val getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    private val translateUseCase: ITranslateUseCase
) : AuthWithCodeController<LoginPayload, RegisterPayload, RegisterCodePayload>(
    loginUseCase,
    registerUseCase,
    createSessionForUserUseCase,
    setSessionForCallUseCase,
    createCodeRegisterUseCase,
    getCodeRegisterUseCase,
    deleteCodeRegisterUseCase
), IAuthController {

    override suspend fun join(call: ApplicationCall, payload: JoinPayload) {
        val code = createCodeInEmailUseCase(payload.email, null) ?: throw ControllerException(
            HttpStatusCode.BadRequest, "auth_join_email_taken"
        )
        val locale = getLocaleForCallUseCase(call)
        sendEmailUseCase(
            Email(
                translateUseCase(locale, "auth_join_email_title"),
                translateUseCase(locale, "auth_join_email_body", listOf(code.code))
            ),
            listOf(payload.email)
        )
    }

    override suspend fun join(call: ApplicationCall, code: String): JoinPayload {
        return getCodeInEmailUseCase(code)?.let {
            JoinPayload(it.email)
        } ?: throw ControllerException(HttpStatusCode.NotFound, "auth_code_invalid")
    }

    override suspend fun join(call: ApplicationCall, code: String, payload: JoinCodePayload) {
        createAssociationUseCase(
            CreateAssociationPayload(
                name = payload.name,
                school = payload.school,
                city = payload.city,
                email = payload.email,
                password = payload.password,
                firstName = payload.firstName,
                lastName = payload.lastName
            )
        ) ?: throw ControllerException(HttpStatusCode.InternalServerError, "error_internal")
        deleteCodeInEmailUseCase(payload.code)
    }

}
