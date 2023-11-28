package me.nathanfallet.suitebde.usecases.users

import io.ktor.server.application.*
import io.ktor.util.*
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.application.IGetJWTPrincipalForCallUseCase
import me.nathanfallet.suitebde.usecases.application.IGetSessionForCallUseCase

class GetUserForCallUseCase(
    private val getJWTPrincipalForCall: IGetJWTPrincipalForCallUseCase,
    private val getSessionForCallUseCase: IGetSessionForCallUseCase,
    private val getUserUseCase: IGetUserUseCase
) : IGetUserForCallUseCase {

    private data class UserForCall(
        val user: User?
    )

    private val userKey = AttributeKey<UserForCall>("suitebde-user")

    override suspend fun invoke(input: ApplicationCall): User? {
        // Note: we cannot use `computeIfAbsent` because it does not support suspending functions
        return input.attributes.getOrNull(userKey)?.user ?: run {
            val id = getJWTPrincipalForCall(input)?.subject ?: getSessionForCallUseCase(input)?.userId
            val computed = UserForCall(id?.let { getUserUseCase(it) })
            input.attributes.put(userKey, computed)
            computed.user
        }
    }

}
