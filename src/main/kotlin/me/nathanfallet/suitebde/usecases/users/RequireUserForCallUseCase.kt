package me.nathanfallet.suitebde.usecases.users

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktor.routers.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.users.User

class RequireUserForCallUseCase(
    private val getUserForCallUseCase: IGetUserForCallUseCase
) : IRequireUserForCallUseCase {

    override suspend fun invoke(input: ApplicationCall): User {
        return getUserForCallUseCase(input) ?: throw ControllerException(
            HttpStatusCode.Unauthorized, "auth_invalid_credentials"
        )
    }

}
