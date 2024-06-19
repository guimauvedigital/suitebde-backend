package com.suitebde.usecases.associations

import com.suitebde.models.associations.Association
import dev.kaccelero.commons.exceptions.ControllerException
import io.ktor.http.*
import io.ktor.server.application.*

class RequireAssociationForCallUseCase(
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
) : IRequireAssociationForCallUseCase {

    override suspend fun invoke(input: ApplicationCall): Association {
        return getAssociationForCallUseCase(input) ?: throw ControllerException(
            HttpStatusCode.NotFound, "associations_not_found"
        )
    }

}
