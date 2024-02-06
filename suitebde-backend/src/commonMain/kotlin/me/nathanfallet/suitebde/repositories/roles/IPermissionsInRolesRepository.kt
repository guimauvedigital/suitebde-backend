package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.CreatePermissionInRole
import me.nathanfallet.suitebde.models.roles.PermissionInRole
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IPermissionsInRolesRepository :
    IChildModelSuspendRepository<PermissionInRole, String, CreatePermissionInRole, Unit, String> {

    suspend fun listForUser(userId: String): List<PermissionInRole>

}
