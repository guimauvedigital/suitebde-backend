package com.suitebde.usecases.auth

import com.suitebde.models.auth.RegisterCodePayload
import com.suitebde.models.users.CreateUserPayload
import com.suitebde.models.users.User
import com.suitebde.usecases.associations.IGetCodeInEmailUseCase
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class RegisterUseCase(
    private val getCodeInEmailUseCase: IGetCodeInEmailUseCase,
    private val createUserUseCase: ICreateChildModelSuspendUseCase<User, CreateUserPayload, UUID>,
) : IRegisterUseCase {

    override suspend fun invoke(input1: String, input2: RegisterCodePayload): User? {
        val codePayload = getCodeInEmailUseCase(input1)
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
