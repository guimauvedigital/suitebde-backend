package com.suitebde.repositories.roles

import com.suitebde.models.roles.CreatePermissionInRolePayload
import com.suitebde.models.roles.Permission
import com.suitebde.models.roles.PermissionInRole
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface IPermissionsInRolesRemoteRepository {

    suspend fun list(pagination: Pagination, roleId: UUID, associationId: UUID): List<PermissionInRole>
    suspend fun create(payload: CreatePermissionInRolePayload, roleId: UUID, associationId: UUID): PermissionInRole?
    suspend fun delete(permission: Permission, roleId: UUID, associationId: UUID): Boolean

}
