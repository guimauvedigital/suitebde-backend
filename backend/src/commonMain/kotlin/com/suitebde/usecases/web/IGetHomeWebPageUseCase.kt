package com.suitebde.usecases.web

import com.suitebde.models.web.WebPage
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.ISuspendUseCase

interface IGetHomeWebPageUseCase : ISuspendUseCase<UUID, WebPage?>
