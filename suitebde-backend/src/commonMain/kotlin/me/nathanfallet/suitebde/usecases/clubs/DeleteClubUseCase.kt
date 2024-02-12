package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.repositories.clubs.IClubsRepository
import me.nathanfallet.usecases.models.delete.IDeleteChildModelSuspendUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase

class DeleteClubUseCase(
    private val repository: IClubsRepository,
    private val listUsersInClubUseCase: IListChildModelSuspendUseCase<UserInClub, String>,
    private val deleteUserInClubUseCase: IDeleteChildModelSuspendUseCase<UserInClub, String, String>,
) : IDeleteChildModelSuspendUseCase<Club, String, String> {

    override suspend fun invoke(input1: String, input2: String): Boolean {
        return repository.delete(input1, input2).also { success ->
            if (!success) return@also
            listUsersInClubUseCase(input1).forEach {
                deleteUserInClubUseCase(it.id, input1)
            }
        }
    }

}
