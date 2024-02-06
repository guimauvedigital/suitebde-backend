package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.CreatePermissionInRole
import me.nathanfallet.suitebde.models.roles.PermissionInRole

interface IPermissionsInRolesRemoteRepository {

    suspend fun list(limit: Long, offset: Long, roleId: String, associationId: String): List<PermissionInRole>
    suspend fun create(payload: CreatePermissionInRole, roleId: String, associationId: String): PermissionInRole?
    suspend fun delete(userId: String, roleId: String, associationId: String): Boolean

}
