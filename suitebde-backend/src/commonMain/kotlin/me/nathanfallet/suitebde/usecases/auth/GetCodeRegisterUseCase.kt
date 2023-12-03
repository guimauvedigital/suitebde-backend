package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import me.nathanfallet.ktorx.usecases.auth.IGetCodeRegisterUseCase
import me.nathanfallet.suitebde.models.auth.RegisterPayload
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.associations.IGetCodeInEmailUseCase

class GetCodeRegisterUseCase(
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    private val getCodeInEmailUseCase: IGetCodeInEmailUseCase,
) : IGetCodeRegisterUseCase<RegisterPayload> {

    override suspend fun invoke(input1: ApplicationCall, input2: String): RegisterPayload? {
        val association = getAssociationForCallUseCase(input1) ?: return null
        val code = getCodeInEmailUseCase(input2)?.takeIf {
            it.associationId == association.id
        } ?: return null
        return RegisterPayload(code.email)
    }

}
