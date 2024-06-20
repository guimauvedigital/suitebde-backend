package com.suitebde.usecases.clubs

import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.RoleInClub
import com.suitebde.models.clubs.UserInClub
import com.suitebde.repositories.clubs.IClubsRepository
import dev.kaccelero.commons.repositories.IDeleteChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.models.UUID

class DeleteClubUseCase(
    private val repository: IClubsRepository,
    private val listUsersInClubUseCase: IListChildModelSuspendUseCase<UserInClub, UUID>,
    private val deleteUserInClubUseCase: IDeleteChildModelSuspendUseCase<UserInClub, UUID, UUID>,
    private val listRolesInClubUseCase: IListChildModelSuspendUseCase<RoleInClub, UUID>,
    private val deleteRoleInClubUseCase: IDeleteChildModelSuspendUseCase<RoleInClub, UUID, UUID>,
) : IDeleteChildModelSuspendUseCase<Club, UUID, UUID> {

    override suspend fun invoke(input1: UUID, input2: UUID): Boolean {
        return repository.delete(input1, input2).also { success ->
            if (!success) return@also
            listUsersInClubUseCase(input1).forEach {
                deleteUserInClubUseCase(it.id, input1)
            }
            listRolesInClubUseCase(input1).forEach {
                deleteRoleInClubUseCase(it.id, input1)
            }
        }
    }

}
