package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
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

    override suspend fun invoke(input: Pair<CreateAssociationPayload, Instant>): Association? {
        val expiresAt = input.second.plus(1, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())
        val association = associationsRepository.createAssociation(
            input.first.name,
            input.first.school,
            input.first.city,
            false,
            input.second,
            expiresAt
        ) ?: return null
        usersRepository.createUser(
            association.id,
            input.first.email,
            hashPasswordUseCase(input.first.password),
            input.first.firstName,
            input.first.lastName,
            true
        )
        return association
    }

}