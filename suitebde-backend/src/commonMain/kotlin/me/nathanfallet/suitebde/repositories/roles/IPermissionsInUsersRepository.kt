package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.Permission

interface IPermissionsInUsersRepository {

    suspend fun getPermissionsForUser(userId: String): List<Permission>

}
