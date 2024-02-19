package me.nathanfallet.suitebde.controllers.root

import io.ktor.server.freemarker.*
import me.nathanfallet.ktorx.routers.templates.LocalizedTemplateUnitRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase

class RootRouter(
    controller: IRootController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
) : LocalizedTemplateUnitRouter(
    controller,
    IRootController::class,
    { template, model ->
        respondTemplate(template, model)
    },
    getLocaleForCallUseCase,
    "root/error.ftl"
)
