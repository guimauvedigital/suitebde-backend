package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.CreatePermissionInRolePayload
import me.nathanfallet.suitebde.models.roles.PermissionInRole
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IPermissionsInRolesRepository :
    IChildModelSuspendRepository<PermissionInRole, String, CreatePermissionInRolePayload, Unit, String> {

    suspend fun listForUser(userId: String, associationId: String): List<PermissionInRole>

}
