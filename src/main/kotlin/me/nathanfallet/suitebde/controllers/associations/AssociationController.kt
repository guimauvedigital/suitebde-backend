package me.nathanfallet.suitebde.controllers.associations

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nathanfallet.suitebde.controllers.IController
import me.nathanfallet.suitebde.models.associations.Association

class AssociationController(

) : IController {

    override fun createRoutes(root: Route) {
        root.route("/api/v1/associations") {
            get {
                call.respond(getAll())
            }
        }
    }

    suspend fun getAll(): List<Association> {
        TODO("Not yet implemented")
    }

}