package me.nathanfallet.suitebde.controllers.associations

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nathanfallet.suitebde.controllers.IRouter

class AssociationRouter(
    private val controller: IAssociationController
) : IRouter {

    override fun createRoutes(root: Route) {
        root.route("/api/v1/associations") {
            createAPIv1GetRoute(this)
        }
    }

    fun createAPIv1GetRoute(root: Route) {
        root.get {
            call.respond(controller.getAll())
        }
    }

}