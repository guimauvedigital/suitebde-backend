package me.nathanfallet.suitebde.usecases.associations

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.models.associations.CreateAssociationPayload
import me.nathanfallet.suitebde.models.users.CreateUserPayload
import me.nathanfallet.suitebde.repositories.IAssociationsRepository
import me.nathanfallet.suitebde.usecases.users.ICreateUserUseCase

class CreateAssociationUseCase(
    private val associationsRepository: IAssociationsRepository,
    private val createUserUseCase: ICreateUserUseCase,
) : ICreateAssociationUseCase {

    override suspend fun invoke(input1: CreateAssociationPayload, input2: Instant): Association? {
        val expiresAt = input2.plus(1, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())
        val association = associationsRepository.createAssociation(
            input1.name,
            input1.school,
            input1.city,
            false,
            input2,
            expiresAt
        ) ?: return null
        createUserUseCase(
            CreateUserPayload(
                association.id,
                input1.email,
                input1.password,
                input1.firstName,
                input1.lastName,
                true
            ), input2
        )
        return association
    }

}