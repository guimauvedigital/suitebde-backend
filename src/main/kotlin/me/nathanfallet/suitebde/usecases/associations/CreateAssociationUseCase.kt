package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
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
        val createdAt = Clock.System.now()
        val expiresAt = createdAt.plus(1, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())
        return associationsRepository.createAssociation(
            input.name,
            input.school,
            input.city,
            false,
            createdAt,
            expiresAt
        )?.also {
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