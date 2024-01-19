package me.nathanfallet.suitebde.repositories.clubs

import me.nathanfallet.suitebde.models.clubs.CreateUserInClub
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.usecases.models.repositories.IChildModelSuspendRepository

interface IUsersInClubsRepository : IChildModelSuspendRepository<UserInClub, String, CreateUserInClub, Unit, String> {

    suspend fun listForUser(userId: String): List<UserInClub>

}
