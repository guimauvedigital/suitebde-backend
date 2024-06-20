package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import com.suitebde.repositories.associations.IAssociationsRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*

class GetAssociationForCallUseCase(
    private val repository: IAssociationsRepository,
) : IGetAssociationForCallUseCase {

    private data class AssociationForCall(
        val association: Association?,
    )

    private val associationKey = AttributeKey<AssociationForCall>("suitebde-association")

    override suspend fun invoke(input: ApplicationCall): Association? {
        // Note: we cannot use `computeIfAbsent` because it does not support suspending functions
        return input.attributes.getOrNull(associationKey)?.association ?: run {
            val computed = AssociationForCall(repository.getAssociationForDomain(input.request.host()))
            input.attributes.put(associationKey, computed)
            computed.association
        }
    }

}
