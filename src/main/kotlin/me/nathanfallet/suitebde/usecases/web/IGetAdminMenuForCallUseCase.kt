package me.nathanfallet.suitebde.usecases.web

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.usecases.pair.IPairSuspendUseCase
import java.util.*

interface IGetAdminMenuForCallUseCase : IPairSuspendUseCase<ApplicationCall, Locale, List<WebMenu>>
