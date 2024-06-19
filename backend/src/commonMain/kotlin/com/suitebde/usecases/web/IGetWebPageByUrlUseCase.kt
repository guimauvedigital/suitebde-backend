package com.suitebde.usecases.web

import com.suitebde.models.web.WebPage
import dev.kaccelero.models.UUID
import dev.kaccelero.usecases.IPairSuspendUseCase

interface IGetWebPageByUrlUseCase : IPairSuspendUseCase<String, UUID, WebPage?>
