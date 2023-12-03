package me.nathanfallet.suitebde.usecases.auth

import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import me.nathanfallet.usecases.base.IUseCase

interface IGetJWTPrincipalForCallUseCase : IUseCase<ApplicationCall, JWTPrincipal?>
