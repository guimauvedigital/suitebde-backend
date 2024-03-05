package me.nathanfallet.suitebde.repositories.clubs

import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.usecases.pagination.Pagination

interface IUsersInClubsRemoteRepository {

    suspend fun list(pagination: Pagination, clubId: String, associationId: String): List<UserInClub>
    suspend fun create(payload: CreateUserInClubPayload, clubId: String, associationId: String): UserInClub?
    suspend fun delete(userId: String, clubId: String, associationId: String): Boolean

}
