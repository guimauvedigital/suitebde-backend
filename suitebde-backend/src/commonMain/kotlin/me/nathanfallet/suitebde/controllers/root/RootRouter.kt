package me.nathanfallet.suitebde.controllers.root

import io.ktor.server.freemarker.*
import me.nathanfallet.ktorx.routers.templates.LocalizedTemplateUnitRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.suitebde.usecases.web.IGetRootMenuUseCase

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
