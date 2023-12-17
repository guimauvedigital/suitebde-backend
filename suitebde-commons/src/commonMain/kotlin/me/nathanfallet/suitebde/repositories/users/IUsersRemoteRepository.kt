package me.nathanfallet.suitebde.repositories.users

import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User

interface IUsersRemoteRepository {

    suspend fun list(): List<User>
    suspend fun get(id: String): User?
    suspend fun update(id: String, payload: UpdateUserPayload): User?

}
