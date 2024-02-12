package me.nathanfallet.suitebde.repositories.clubs

import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IUsersInClubsRepository :
    IChildModelSuspendRepository<UserInClub, String, CreateUserInClubPayload, Unit, String> {

    suspend fun listForUser(userId: String, associationId: String): List<UserInClub>

}
