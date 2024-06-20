package com.suitebde.controllers.root

import com.suitebde.usecases.associations.IGetAssociationForCallUseCase
import com.suitebde.usecases.web.IGetHomeWebPageUseCase
import dev.kaccelero.commons.responses.RedirectResponse
import io.ktor.server.application.*

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
