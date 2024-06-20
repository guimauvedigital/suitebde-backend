package com.suitebde.repositories.clubs

import com.suitebde.models.clubs.CreateRoleInClubPayload
import com.suitebde.models.clubs.RoleInClub
import com.suitebde.models.clubs.UpdateRoleInClubPayload
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IRolesInClubsRepository :
    IChildModelSuspendRepository<RoleInClub, UUID, CreateRoleInClubPayload, UpdateRoleInClubPayload, UUID> {

    suspend fun getDefault(parentId: UUID): RoleInClub?

}
