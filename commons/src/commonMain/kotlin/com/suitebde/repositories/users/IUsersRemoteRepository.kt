package com.suitebde.repositories.users

import com.suitebde.models.roles.Permission
import com.suitebde.models.users.UpdateUserPayload
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface IUsersRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: UUID): List<User>
    suspend fun get(id: UUID, associationId: UUID): User?
    suspend fun update(id: UUID, payload: UpdateUserPayload, associationId: UUID): User?
    suspend fun delete(id: UUID, associationId: UUID): Boolean
    suspend fun listPermissions(id: UUID, associationId: UUID): List<Permission>

}
