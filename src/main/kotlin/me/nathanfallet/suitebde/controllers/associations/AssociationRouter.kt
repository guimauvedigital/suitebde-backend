package me.nathanfallet.suitebde.controllers.associations

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
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
        root.route("/admin") {
            createAdminGetRoute(this)
        }
    }

    fun createAPIv1GetRoute(root: Route) {
        root.get {
            call.respond(controller.getAll())
        }
    }

    fun createAdminGetRoute(root: Route) {
        root.get {
            call.respond(
                FreeMarkerContent(
                    "admin/home.ftl",
                    null
                )
            )
        }
    }

}