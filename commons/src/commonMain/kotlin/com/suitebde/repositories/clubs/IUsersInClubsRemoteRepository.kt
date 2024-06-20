package com.suitebde.repositories.clubs

import com.suitebde.models.clubs.CreateUserInClubPayload
import com.suitebde.models.clubs.UserInClub
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

interface IUsersInClubsRemoteRepository {

    suspend fun list(pagination: Pagination, clubId: UUID, associationId: UUID): List<UserInClub>
    suspend fun create(payload: CreateUserInClubPayload, clubId: UUID, associationId: UUID): UserInClub?
    suspend fun delete(userId: UUID, clubId: UUID, associationId: UUID): Boolean

}
