package me.nathanfallet.suitebde.controllers.auth

import io.ktor.http.*
import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.models.LocalizedString
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

class AuthController(
    private val loginUseCase: ILoginUseCase,
    private val createCodeInEmailUseCase: ICreateCodeInEmailUseCase,
    private val getCodeInEmailUseCase: IGetCodeInEmailUseCase,
    private val deleteCodeInEmailUseCase: IDeleteCodeInEmailUseCase,
    private val createAssociationUseCase: ICreateAssociationUseCase,
    private val sendEmailUseCase: ISendEmailUseCase
) : IAuthController {

    override suspend fun login(payload: LoginPayload): User {
        return loginUseCase(payload)
    }

    override suspend fun join(payload: JoinPayload, joiningAt: Instant) {
        val code =
            createCodeInEmailUseCase(Triple(payload.email, null, joiningAt)) ?: throw ControllerException(
                HttpStatusCode.BadRequest,
                LocalizedString.AUTH_JOIN_EMAIL_TAKEN
            )
        sendEmailUseCase(
            Triple(
                payload.email,
                LocalizedString.AUTH_JOIN_EMAIL_TITLE.value,
                LocalizedString.AUTH_JOIN_EMAIL_BODY.value.format(code.code)
            )
        )
    }

    override suspend fun join(code: String, joiningAt: Instant): JoinPayload {
        return getCodeInEmailUseCase(Pair(code, joiningAt))?.let {
            JoinPayload(it.email)
        } ?: throw ControllerException(HttpStatusCode.NotFound, LocalizedString.AUTH_JOIN_CODE_INVALID)
    }

    override suspend fun join(payload: JoinCodePayload, joiningAt: Instant) {
        createAssociationUseCase(
            Pair(
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
            )
        ) ?: throw ControllerException(HttpStatusCode.InternalServerError, LocalizedString.ERROR_INTERNAL)
        deleteCodeInEmailUseCase(payload.code)
    }

}