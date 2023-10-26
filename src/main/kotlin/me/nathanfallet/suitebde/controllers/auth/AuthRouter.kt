package me.nathanfallet.suitebde.controllers.auth

import io.ktor.server.routing.*
import me.nathanfallet.suitebde.controllers.IRouter

class AuthRouter(
    private val controller: IAuthController
) : IRouter {

    override fun createRoutes(root: Route) {

    }

}