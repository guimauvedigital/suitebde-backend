package me.nathanfallet.suitebde.usecases.users

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.usecases.application.IGetSessionForCallUseCase

class GetUserForCallUseCase(
    private val repository: IUsersRepository,
    private val getSessionForCallUseCase: IGetSessionForCallUseCase
) : IGetUserForCallUseCase {

    override suspend fun invoke(input: ApplicationCall): User? {
        val id = input.principal<JWTPrincipal>()?.subject ?: getSessionForCallUseCase(input)?.userId
        return id?.let { repository.getUser(it) }
    }

}