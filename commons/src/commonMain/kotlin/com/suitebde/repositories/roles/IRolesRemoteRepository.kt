package com.suitebde.repositories.roles

import com.suitebde.models.roles.CreateRolePayload
import com.suitebde.models.roles.Role
import com.suitebde.models.roles.UpdateRolePayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface IRolesRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: UUID): List<Role>
    suspend fun get(id: UUID, associationId: UUID): Role?
    suspend fun create(payload: CreateRolePayload, associationId: UUID): Role?
    suspend fun update(id: UUID, payload: UpdateRolePayload, associationId: UUID): Role?
    suspend fun delete(id: UUID, associationId: UUID): Boolean

}
