package me.nathanfallet.suitebde.usecases.auth

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.ktorx.usecases.auth.ICreateCodeRegisterUseCase
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.models.application.Email
import me.nathanfallet.suitebde.models.auth.RegisterPayload
import me.nathanfallet.suitebde.usecases.associations.ICreateCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.usecases.emails.ISendEmailUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class CreateCodeRegisterUseCase(
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    private val createCodeInEmailUseCase: ICreateCodeInEmailUseCase,
    private val sendEmailUseCase: ISendEmailUseCase,
    private val translateUseCase: ITranslateUseCase,
    private val getLocaleForCallUseCase: IGetLocaleForCallUseCase
) : ICreateCodeRegisterUseCase<RegisterPayload> {

    override suspend fun invoke(input1: ApplicationCall, input2: RegisterPayload): String {
        val association = getAssociationForCallUseCase(input1) ?: throw ControllerException(
            HttpStatusCode.NotFound, "auth_register_no_association"
        )
        val code = createCodeInEmailUseCase(input2.email, association.id) ?: throw ControllerException(
            HttpStatusCode.BadRequest, "auth_register_email_taken"
        )
        val locale = getLocaleForCallUseCase(input1)
        sendEmailUseCase(
            Email(
                translateUseCase(locale, "auth_register_email_title"),
                translateUseCase(locale, "auth_register_email_body", listOf(code.code))
            ),
            listOf(input2.email)
        )
        return code.code
    }

}
