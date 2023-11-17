package me.nathanfallet.suitebde.usecases.web

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase

class GetPublicMenuForCallUseCase(
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    private val getWebMenusUseCase: IGetWebMenusUseCase
) : IGetPublicMenuForCallUseCase {

    override suspend fun invoke(input: ApplicationCall): List<WebMenu> {
        return getWebMenusUseCase(requireAssociationForCallUseCase(input).id)
    }

}
