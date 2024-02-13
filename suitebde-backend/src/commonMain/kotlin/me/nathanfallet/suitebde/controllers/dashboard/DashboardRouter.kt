package me.nathanfallet.suitebde.controllers.dashboard

import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase
import me.nathanfallet.ktorx.usecases.users.IRequireUserForCallUseCase
import me.nathanfallet.suitebde.controllers.models.AdminUnitRouter
import me.nathanfallet.suitebde.usecases.web.IGetAdminMenuForCallUseCase
import me.nathanfallet.usecases.localization.ITranslateUseCase

class DashboardRouter(
    controller: IDashboardController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
    translateUseCase: ITranslateUseCase,
    requireUserForCallUseCase: IRequireUserForCallUseCase,
    getAdminMenuForCallUseCase: IGetAdminMenuForCallUseCase,
) : AdminUnitRouter(
    controller,
    IDashboardController::class,
    getLocaleForCallUseCase,
    translateUseCase,
    requireUserForCallUseCase,
    getAdminMenuForCallUseCase
)
