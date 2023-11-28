package me.nathanfallet.suitebde.usecases.web

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.usecases.base.ISuspendUseCase

interface IGetAdminMenuForCallUseCase : ISuspendUseCase<ApplicationCall, List<WebMenu>>
