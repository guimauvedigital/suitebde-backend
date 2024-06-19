package com.suitebde.usecases.web

import com.suitebde.models.web.WebMenu
import com.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import dev.kaccelero.commons.repositories.IListChildModelSuspendUseCase
import dev.kaccelero.models.UUID
import io.ktor.server.application.*

class GetPublicMenuForCallUseCase(
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    private val getWebMenusUseCase: IListChildModelSuspendUseCase<WebMenu, UUID>,
) : IGetPublicMenuForCallUseCase {

    override suspend fun invoke(input: ApplicationCall): List<WebMenu> =
        getWebMenusUseCase(requireAssociationForCallUseCase(input).id)

}
