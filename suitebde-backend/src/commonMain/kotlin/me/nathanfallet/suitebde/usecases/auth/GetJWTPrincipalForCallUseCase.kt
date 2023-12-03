package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

class GetJWTPrincipalForCallUseCase : IGetJWTPrincipalForCallUseCase {

    override fun invoke(input: ApplicationCall): JWTPrincipal? {
        return input.principal()
    }

}
