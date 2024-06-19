package com.suitebde.usecases.clubs

import com.suitebde.models.clubs.*
import com.suitebde.models.users.OptionalUserContext
import com.suitebde.repositories.clubs.IClubsRepository
import dev.kaccelero.commons.repositories.ICreateChildModelSuspendUseCase
import dev.kaccelero.commons.repositories.ICreateChildModelWithContextSuspendUseCase
import dev.kaccelero.models.IContext
import dev.kaccelero.models.UUID

class CreateClubUseCase(
    private val repository: IClubsRepository,
    private val createRoleInClubUseCase: ICreateChildModelSuspendUseCase<RoleInClub, CreateRoleInClubPayload, UUID>,
    private val createUserInClubUseCase: ICreateChildModelWithContextSuspendUseCase<UserInClub, CreateUserInClubPayload, UUID>,
) : ICreateChildModelWithContextSuspendUseCase<Club, CreateClubPayload, UUID> {

    override suspend fun invoke(input1: CreateClubPayload, input2: UUID, input3: IContext): Club? =
        repository.create(input1, input2, input3)?.also { club ->
            createRoleInClubUseCase(
                CreateRoleInClubPayload(input1.memberRoleName, admin = false, default = true),
                club.id
            )
            val adminRole = createRoleInClubUseCase(
                CreateRoleInClubPayload(input1.adminRoleName, admin = true),
                club.id
            )
            (input3 as? OptionalUserContext)?.userId?.let { userId ->
                createUserInClubUseCase(
                    CreateUserInClubPayload(userId),
                    club.id,
                    OptionalRoleInClubContext(adminRole?.id)
                )
            }
        }

}
