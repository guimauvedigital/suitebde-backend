package me.nathanfallet.suitebde.repositories.clubs

import me.nathanfallet.suitebde.models.clubs.CreateRoleInClubPayload
import me.nathanfallet.suitebde.models.clubs.RoleInClub
import me.nathanfallet.suitebde.models.clubs.UpdateRoleInClubPayload
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IRolesInClubsRepository :
    IChildModelSuspendRepository<RoleInClub, String, CreateRoleInClubPayload, UpdateRoleInClubPayload, String> {

    suspend fun getDefault(parentId: String): RoleInClub?

}
