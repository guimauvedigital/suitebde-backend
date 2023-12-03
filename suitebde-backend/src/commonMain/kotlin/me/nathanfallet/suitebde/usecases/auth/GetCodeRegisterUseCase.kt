package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import me.nathanfallet.ktorx.usecases.auth.IGetCodeRegisterUseCase
import me.nathanfallet.suitebde.models.auth.RegisterPayload
import me.nathanfallet.suitebde.usecases.associations.IGetCodeInEmailUseCase
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase

class GetCodeRegisterUseCase(
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    private val getCodeInEmailUseCase: IGetCodeInEmailUseCase,
) : IGetCodeRegisterUseCase<RegisterPayload> {

    override suspend fun invoke(input1: ApplicationCall, input2: String): RegisterPayload? {
        val code = getCodeInEmailUseCase(input2)?.takeIf {
            it.associationId == requireAssociationForCallUseCase(input1).id
        } ?: return null
        return RegisterPayload(code.email)
    }

}
