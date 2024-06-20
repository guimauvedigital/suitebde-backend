package com.suitebde.repositories.clubs

import com.suitebde.models.clubs.CreateUserInClubPayload
import com.suitebde.models.clubs.UserInClub
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.IChildModelSuspendRepository

interface IUsersInClubsRepository :
    IChildModelSuspendRepository<UserInClub, UUID, CreateUserInClubPayload, Unit, UUID> {

    suspend fun listForUser(userId: UUID, associationId: UUID): List<UserInClub>

}
