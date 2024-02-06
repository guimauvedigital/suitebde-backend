package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.CreatePermissionInRolePayload
import me.nathanfallet.suitebde.models.roles.PermissionInRole

interface IPermissionsInRolesRemoteRepository {

    suspend fun list(limit: Long, offset: Long, roleId: String, associationId: String): List<PermissionInRole>
    suspend fun create(payload: CreatePermissionInRolePayload, roleId: String, associationId: String): PermissionInRole?
    suspend fun delete(userId: String, roleId: String, associationId: String): Boolean

}
