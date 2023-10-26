package me.nathanfallet.suitebde.controllers.auth

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nathanfallet.suitebde.controllers.IRouter

class AuthRouter(
    private val controller: IAuthController
) : IRouter {

    override fun createRoutes(root: Route) {
        root.route("/auth") {
            createLoginRoute(this)
            createRegisterRoute(this)
        }
    }

    fun createLoginRoute(root: Route) {
        root.get("/login") {
            call.respond(
                FreeMarkerContent(
                    "auth/login.ftl",
                    null
                )
            )
        }
    }

    fun createRegisterRoute(root: Route) {
        root.get("/register") {
            call.respond(
                FreeMarkerContent(
                    "auth/register.ftl",
                    null
                )
            )
        }
    }

}