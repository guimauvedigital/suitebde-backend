package me.nathanfallet.suitebde.controllers.auth

import me.nathanfallet.ktorx.routers.api.APIUnitRouter
import me.nathanfallet.ktorx.routers.concat.ConcatUnitRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase

class AuthRouter(
    controller: IAuthController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
) : ConcatUnitRouter(
    AuthWithJoinRouter(
        controller,
        getLocaleForCallUseCase
    ),
    APIUnitRouter(
        controller,
        IAuthController::class,
        route = "/auth",
        prefix = "/api/v1"
    )
)
