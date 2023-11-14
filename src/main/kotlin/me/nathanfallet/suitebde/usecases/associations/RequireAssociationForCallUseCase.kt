package me.nathanfallet.suitebde.usecases.associations

import io.ktor.http.*
import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.exceptions.ControllerException
import me.nathanfallet.suitebde.models.associations.Association

class RequireAssociationForCallUseCase(
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase
) : IRequireAssociationForCallUseCase {

    override suspend fun invoke(input: ApplicationCall): Association {
        return getAssociationForCallUseCase(input) ?: throw ControllerException(
            HttpStatusCode.NotFound, "associations_not_found"
        )
    }

}
