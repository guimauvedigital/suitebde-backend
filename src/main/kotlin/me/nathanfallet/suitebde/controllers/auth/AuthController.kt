package me.nathanfallet.suitebde.controllers.auth

import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.auth.JoinCodePayload
import me.nathanfallet.suitebde.models.auth.JoinPayload
import me.nathanfallet.suitebde.models.auth.LoginPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.associations.ICreateAssociationUseCase
import me.nathanfallet.suitebde.usecases.auth.ILoginUseCase

class AuthController(
    private val loginUseCase: ILoginUseCase,
    private val createAssociationUseCase: ICreateAssociationUseCase
) : IAuthController {

    override suspend fun login(payload: LoginPayload): User {
        return loginUseCase(payload)
    }

    override suspend fun join(payload: JoinPayload) {

    }

    override suspend fun join(code: String): JoinPayload {
        return JoinPayload("email")
    }

    override suspend fun join(payload: JoinCodePayload) {
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
        )
    }

}