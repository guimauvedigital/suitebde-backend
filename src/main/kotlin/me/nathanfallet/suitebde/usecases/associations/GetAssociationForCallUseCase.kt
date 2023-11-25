package me.nathanfallet.suitebde.usecases.associations

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository

class GetAssociationForCallUseCase(
    private val repository: IAssociationsRepository
) : IGetAssociationForCallUseCase {

    private data class AssociationForCall(
        val association: Association?
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
