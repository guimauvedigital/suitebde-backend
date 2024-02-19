package me.nathanfallet.suitebde.controllers.root

import io.ktor.server.application.*
import me.nathanfallet.ktorx.models.responses.RedirectResponse
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetHomeWebPageUseCase

class RootController(
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
    private val getHomeWebPageUseCase: IGetHomeWebPageUseCase,
) : IRootController {

    override suspend fun redirect(call: ApplicationCall): RedirectResponse =
        getAssociationForCallUseCase(call)?.let { association ->
            getHomeWebPageUseCase(association.id)?.let {
                RedirectResponse("/pages/${it.url}")
            }
        } ?: RedirectResponse("/home")

    override fun home() {}

}
