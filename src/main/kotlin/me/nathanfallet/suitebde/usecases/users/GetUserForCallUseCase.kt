package me.nathanfallet.suitebde.usecases.users

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository

class GetUserForCallUseCase(
    private val repository: IUsersRepository
) : IGetUserForCallUseCase {

    override suspend fun invoke(input: ApplicationCall): User? {
        return input.principal<JWTPrincipal>()?.subject?.let {
            repository.getUser(it)
        }
    }

}