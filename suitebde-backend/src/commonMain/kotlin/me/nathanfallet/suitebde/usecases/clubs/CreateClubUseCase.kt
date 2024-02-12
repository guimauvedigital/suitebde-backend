package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateClubPayload
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.models.users.OptionalUserContext
import me.nathanfallet.suitebde.repositories.clubs.IClubsRepository
import me.nathanfallet.usecases.context.IContext
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.create.context.ICreateChildModelWithContextSuspendUseCase

class CreateClubUseCase(
    private val repository: IClubsRepository,
    private val createUserInClubUseCase: ICreateChildModelSuspendUseCase<UserInClub, CreateUserInClubPayload, String>,
) : ICreateChildModelWithContextSuspendUseCase<Club, CreateClubPayload, String> {

    override suspend fun invoke(input1: CreateClubPayload, input2: String, input3: IContext): Club? {
        return repository.create(input1, input2, input3)?.also { club ->
            (input3 as? OptionalUserContext)?.userId?.let { userId ->
                createUserInClubUseCase(CreateUserInClubPayload(userId), club.id)
            }
        }
    }

}
