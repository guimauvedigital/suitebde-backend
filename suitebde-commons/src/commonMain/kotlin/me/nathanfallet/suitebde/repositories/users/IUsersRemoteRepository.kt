package me.nathanfallet.suitebde.repositories.users

import me.nathanfallet.suitebde.models.roles.Permission
import me.nathanfallet.suitebde.models.users.UpdateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.usecases.pagination.Pagination

interface IUsersRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: String): List<User>
    suspend fun get(id: String, associationId: String): User?
    suspend fun update(id: String, payload: UpdateUserPayload, associationId: String): User?
    suspend fun listPermissions(id: String, associationId: String): List<Permission>

}
