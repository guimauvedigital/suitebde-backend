package me.nathanfallet.suitebde.usecases.associations

import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.repositories.IUsersRepository
import me.nathanfallet.suitebde.usecases.auth.IHashPasswordUseCase

class CreateAssociationUseCase(
    private val associationsRepository: IAssociationsRepository,
    private val usersRepository: IUsersRepository,
    private val hashPasswordUseCase: IHashPasswordUseCase
) : ICreateAssociationUseCase {

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