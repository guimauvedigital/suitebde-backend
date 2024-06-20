package com.suitebde.controllers.root

import com.suitebde.usecases.web.IGetRootMenuUseCase
import dev.kaccelero.commons.localization.IGetLocaleForCallUseCase
import dev.kaccelero.routers.LocalizedTemplateUnitRouter
import io.ktor.server.freemarker.*

class RootRouter(
    controller: IRootController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    getRootMenuUseCase: IGetRootMenuUseCase,
) : LocalizedTemplateUnitRouter(
    controller,
    IRootController::class,
    { template, model ->
        respondTemplate(
            template, model + mapOf(
                "menu" to getRootMenuUseCase(this),
            )
        )
    },
    getLocaleForCallUseCase,
    "root/error.ftl"
)
