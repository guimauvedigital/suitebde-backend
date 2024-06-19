package com.suitebde.usecases.web

import com.suitebde.models.web.WebMenu
import dev.kaccelero.usecases.ISuspendUseCase
import io.ktor.server.application.*

interface IGetPublicMenuForCallUseCase : ISuspendUseCase<ApplicationCall, List<WebMenu>>
