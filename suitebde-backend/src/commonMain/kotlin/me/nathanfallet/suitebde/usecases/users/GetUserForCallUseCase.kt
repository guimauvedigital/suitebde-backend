package me.nathanfallet.suitebde.usecases.users

import io.ktor.server.application.*
import io.ktor.util.*
import me.nathanfallet.ktorx.usecases.auth.IGetSessionForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IGetUserForCallUseCase
import me.nathanfallet.suitebde.models.auth.SessionPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.IGetJWTPrincipalForCallUseCase
import me.nathanfallet.usecases.users.IUser

class GetUserForCallUseCase(
    private val getJWTPrincipalForCall: IGetJWTPrincipalForCallUseCase,
    private val getSessionForCallUseCase: IGetSessionForCallUseCase,
    private val getUserUseCase: IGetUserUseCase
) : IGetUserForCallUseCase {

    private data class UserForCall(
        val user: User?
    )

    private val userKey = AttributeKey<UserForCall>("suitebde-user")

    override suspend fun invoke(input: ApplicationCall): IUser? {
        // Note: we cannot use `computeIfAbsent` because it does not support suspending functions
        return input.attributes.getOrNull(userKey)?.user ?: run {
            val id =
                getJWTPrincipalForCall(input)?.subject ?: (getSessionForCallUseCase(input) as? SessionPayload)?.userId
            val computed = UserForCall(id?.let { getUserUseCase(it) })
            input.attributes.put(userKey, computed)
            computed.user
        }
    }

}
