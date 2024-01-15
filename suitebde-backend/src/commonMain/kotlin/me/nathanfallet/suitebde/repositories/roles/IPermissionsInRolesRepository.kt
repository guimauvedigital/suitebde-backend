package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.Permission

interface IPermissionsInRolesRepository {

    suspend fun list(roleId: String): List<Permission>

}
