package me.nathanfallet.suitebde.usecases.web

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.associations.IRequireAssociationForCallUseCase
import me.nathanfallet.usecases.models.list.IListChildModelSuspendUseCase

class GetPublicMenuForCallUseCase(
    private val requireAssociationForCallUseCase: IRequireAssociationForCallUseCase,
    private val getWebMenusUseCase: IListChildModelSuspendUseCase<WebMenu, String>
) : IGetPublicMenuForCallUseCase {

    override suspend fun invoke(input: ApplicationCall): List<WebMenu> {
        return getWebMenusUseCase(requireAssociationForCallUseCase(input).id)
    }

}
