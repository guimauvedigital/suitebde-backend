package me.nathanfallet.suitebde.usecases.web

import io.ktor.server.application.*
import me.nathanfallet.suitebde.models.web.WebMenu
import me.nathanfallet.suitebde.usecases.ISuspendUseCase
import java.util.*

interface IGetAdminMenuForCallUseCase : ISuspendUseCase<Pair<ApplicationCall, Locale>, List<WebMenu>>
