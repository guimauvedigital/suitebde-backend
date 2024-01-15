package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.PermissionInRole

interface IPermissionsInUsersRepository {

    suspend fun getPermissionsForUser(userId: String): List<PermissionInRole>

}
