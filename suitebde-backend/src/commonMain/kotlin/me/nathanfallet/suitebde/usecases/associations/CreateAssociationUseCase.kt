package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository
import me.nathanfallet.usecases.models.create.ICreateChildModelSuspendUseCase
import me.nathanfallet.usecases.models.create.ICreateModelSuspendUseCase

class CreateAssociationUseCase(
    private val associationsRepository: IAssociationsRepository,
    private val createUserUseCase: ICreateChildModelSuspendUseCase<User, CreateUserPayload, String>,
) : ICreateModelSuspendUseCase<Association, CreateAssociationPayload> {

    override suspend fun invoke(input: CreateAssociationPayload): Association? {
        val association = associationsRepository.create(input) ?: return null
        createUserUseCase(
            CreateUserPayload(
                input.email,
                input.password,
                input.firstName,
                input.lastName,
                true
            ),
            association.id
        )
        return association
    }

}
