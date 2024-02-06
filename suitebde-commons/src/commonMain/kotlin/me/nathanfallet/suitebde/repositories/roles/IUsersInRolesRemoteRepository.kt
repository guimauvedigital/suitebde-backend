package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.CreateUserInRolePayload
import me.nathanfallet.suitebde.models.roles.UserInRole

interface IUsersInRolesRemoteRepository {

    suspend fun list(limit: Long, offset: Long, roleId: String, associationId: String): List<UserInRole>
    suspend fun create(payload: CreateUserInRolePayload, roleId: String, associationId: String): UserInRole?
    suspend fun delete(userId: String, roleId: String, associationId: String): Boolean

}
