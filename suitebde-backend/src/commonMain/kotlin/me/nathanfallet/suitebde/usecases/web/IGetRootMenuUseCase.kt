package me.nathanfallet.suitebde.usecases.web

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IGetRootMenuUseCase : ISuspendUseCase<ApplicationCall, List<WebMenu>>
