package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.CreateUserInRole
import me.nathanfallet.suitebde.models.roles.UserInRole
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IUsersInRolesRepository : IChildModelSuspendRepository<UserInRole, String, CreateUserInRole, Unit, String> {

    suspend fun listForUser(userId: String): List<UserInRole>

}
