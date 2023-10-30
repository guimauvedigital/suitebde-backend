package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import me.nathanfallet.suitebde.extensions.invoke
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.usecases.users.ICreateUserUseCase

class CreateAssociationUseCase(
    private val associationsRepository: IAssociationsRepository,
    private val createUserUseCase: ICreateUserUseCase,
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
        createUserUseCase(
            CreateUserPayload(
                association.id,
                input.first.email,
                input.first.password,
                input.first.firstName,
                input.first.lastName,
                true
            ), input.second
        )
        return association
    }

}