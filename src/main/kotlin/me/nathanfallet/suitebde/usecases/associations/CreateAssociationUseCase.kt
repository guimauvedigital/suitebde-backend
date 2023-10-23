package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.usecases.ISuspendUseCase
import me.nathanfallet.suitebde.usecases.users.IHashPasswordUseCase

class CreateAssociationUseCase(
    private val associationsRepository: IAssociationsRepository,
    private val usersRepository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase
) : ISuspendUseCase<CreateAssociationPayload, Association?> {

    override suspend fun invoke(input: CreateAssociationPayload): Association? {
        return associationsRepository.createAssociation(input.name)?.also {
            usersRepository.createUser(
                it.id,
                input.email,
                hashPasswordUseCase(input.password),
                input.firstName,
                input.lastName,
                true
            )
        }
    }

}