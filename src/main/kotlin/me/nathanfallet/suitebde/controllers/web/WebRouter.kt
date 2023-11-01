package me.nathanfallet.suitebde.controllers.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nathanfallet.ktor.routers.routers.IRouter
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForCallUseCase

class WebRouter(
    private val getAssociationForCallUseCase: IGetAssociationForCallUseCase,
) : IRouter {

    override fun createRoutes(root: Route) {
        createGetRoute(root)
    }

    fun createGetRoute(root: Route) {
        root.get {
            val association = getAssociationForCallUseCase(call) ?: run {
                call.respondTemplate(
                        "root/home.ftl",
                        null
                    )
                return@get
            }
            call.respond(HttpStatusCode.NotFound)
        }
    }

}