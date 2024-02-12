package me.nathanfallet.suitebde.repositories.roles

import me.nathanfallet.suitebde.models.roles.CreateUserInRolePayload
import me.nathanfallet.suitebde.models.roles.UserInRole
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IUsersInRolesRepository :
    IChildModelSuspendRepository<UserInRole, String, CreateUserInRolePayload, Unit, String> {

    suspend fun listForUser(userId: String, associationId: String): List<UserInRole>

}
