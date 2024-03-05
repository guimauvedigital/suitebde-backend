package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.CreateRolePayload
import me.nathanfallet.suitebde.models.roles.Role
import me.nathanfallet.suitebde.models.roles.UpdateRolePayload
import me.nathanfallet.usecases.pagination.Pagination

interface IRolesRemoteRepository {

    suspend fun list(pagination: Pagination, associationId: String): List<Role>
    suspend fun get(id: String, associationId: String): Role?
    suspend fun create(payload: CreateRolePayload, associationId: String): Role?
    suspend fun update(id: String, payload: UpdateRolePayload, associationId: String): Role?
    suspend fun delete(id: String, associationId: String): Boolean

}
