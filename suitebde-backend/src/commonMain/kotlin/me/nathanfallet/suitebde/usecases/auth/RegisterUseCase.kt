package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import me.nathanfallet.ktorx.usecases.auth.IRegisterUseCase
import me.nathanfallet.suitebde.models.auth.RegisterCodePayload
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.IGetCodeInEmailUseCase
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.users.IUser

class RegisterUseCase(
    private val getCodeInEmailUseCase: IGetCodeInEmailUseCase,
    private val createUserUseCase: ICreateChildModelSuspendUseCase<User, CreateUserPayload, String>,
) : IRegisterUseCase<RegisterCodePayload> {

    override suspend fun invoke(input1: ApplicationCall, input2: RegisterCodePayload): IUser? {
        val codePayload = getCodeInEmailUseCase(input1.parameters["code"]!!)
            ?.takeIf { it.associationId != null } ?: return null
        return createUserUseCase(
            CreateUserPayload(
                email = codePayload.email,
                password = input2.password,
                firstName = input2.firstName,
                lastName = input2.lastName,
                superuser = false,
            ),
            codePayload.associationId!!
        )
    }

}
