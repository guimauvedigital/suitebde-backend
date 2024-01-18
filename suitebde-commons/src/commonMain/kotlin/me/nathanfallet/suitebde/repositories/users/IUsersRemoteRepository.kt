package me.nathanfallet.suitebde.repositories.users

import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User

interface IUsersRemoteRepository {

    suspend fun list(limit: Long, offset: Long, associationId: String): List<User>
    suspend fun get(id: String, associationId: String): User?
    suspend fun update(id: String, payload: UpdateUserPayload, associationId: String): User?

}
