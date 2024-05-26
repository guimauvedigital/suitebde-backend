package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.models.clubs.*
import me.nathanfallet.suitebde.models.users.OptionalUserContext
import me.nathanfallet.suitebde.repositories.clubs.IClubsRepository
import me.nathanfallet.usecases.context.IContext
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.create.context.ICreateChildModelWithContextSuspendUseCase

class CreateClubUseCase(
    private val repository: IClubsRepository,
    private val createRoleInClubUseCase: ICreateChildModelSuspendUseCase<RoleInClub, CreateRoleInClubPayload, String>,
    private val createUserInClubUseCase: ICreateChildModelWithContextSuspendUseCase<UserInClub, CreateUserInClubPayload, String>,
) : ICreateChildModelWithContextSuspendUseCase<Club, CreateClubPayload, String> {

    override suspend fun invoke(input1: CreateClubPayload, input2: String, input3: IContext): Club? =
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
