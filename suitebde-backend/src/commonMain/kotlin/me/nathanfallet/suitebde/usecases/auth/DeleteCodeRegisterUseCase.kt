package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import me.nathanfallet.ktorx.usecases.auth.IDeleteCodeRegisterUseCase
import me.nathanfallet.suitebde.usecases.associations.IDeleteCodeInEmailUseCase

class DeleteCodeRegisterUseCase(
    private val deleteCodeInEmailUseCase: IDeleteCodeInEmailUseCase
) : IDeleteCodeRegisterUseCase {

    override suspend fun invoke(input1: ApplicationCall, input2: String) {
        deleteCodeInEmailUseCase(input2)
    }

}
