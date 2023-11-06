package me.nathanfallet.suitebde.usecases.associations

import io.ktor.server.application.*
import io.ktor.server.request.*
import me.nathanfallet.suitebde.models.associations.Association
import me.nathanfallet.suitebde.repositories.associations.IAssociationsRepository

class GetAssociationForCallUseCase(
    private val repository: IAssociationsRepository
) : IGetAssociationForCallUseCase {

    override suspend fun invoke(input: ApplicationCall): Association? {
        return repository.getAssociationForDomain(input.request.host())
    }

}