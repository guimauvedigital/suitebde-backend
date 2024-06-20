package com.suitebde.repositories.roles

import com.suitebde.models.roles.CreatePermissionInRolePayload
import com.suitebde.models.roles.PermissionInRole
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IPermissionsInRolesRepository :
    IChildModelSuspendRepository<PermissionInRole, String, CreatePermissionInRolePayload, Unit, UUID> {

    suspend fun listForUser(userId: UUID, associationId: UUID): List<PermissionInRole>

}
