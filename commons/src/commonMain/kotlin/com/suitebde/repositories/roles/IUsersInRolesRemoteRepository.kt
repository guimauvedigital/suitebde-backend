package com.suitebde.repositories.roles

import com.suitebde.models.roles.CreateUserInRolePayload
import com.suitebde.models.roles.UserInRole
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface IUsersInRolesRemoteRepository {

    suspend fun list(pagination: Pagination, roleId: UUID, associationId: UUID): List<UserInRole>
    suspend fun create(payload: CreateUserInRolePayload, roleId: UUID, associationId: UUID): UserInRole?
    suspend fun delete(userId: UUID, roleId: UUID, associationId: UUID): Boolean

}
