package me.nathanfallet.suitebde.controllers.auth

import me.nathanfallet.ktorx.routers.auth.AuthAPIRouter
import me.nathanfallet.ktorx.routers.concat.ConcatUnitRouter
import me.nathanfallet.ktorx.usecases.localization.IGetLocaleForCallUseCase

class AuthRouter(
    controller: IAuthController,
    getLocaleForCallUseCase: IGetLocaleForCallUseCase,
) : ConcatUnitRouter(
    listOf(
        AuthWithJoinRouter(
            controller,
            getLocaleForCallUseCase
        ),
        AuthAPIRouter(
            controller,
            prefix = "/api/v1"
        )
    )
)
