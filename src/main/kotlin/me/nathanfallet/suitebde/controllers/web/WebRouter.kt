package me.nathanfallet.suitebde.controllers.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nathanfallet.suitebde.controllers.IRouter
import me.nathanfallet.suitebde.usecases.associations.IGetAssociationForDomainUseCase

class WebRouter(
    private val getAssociationForDomainUseCase: IGetAssociationForDomainUseCase,
) : IRouter {

    override fun createRoutes(root: Route) {
        createGetRoute(root)
    }

    fun createGetRoute(root: Route) {
        root.get {
            val association = getAssociationForDomainUseCase(call.request.host()) ?: run {
                call.respond(
                    FreeMarkerContent(
                        "root/home.ftl",
                        null
                    )
                )
                return@get
            }
            call.respond(HttpStatusCode.NotFound)
        }
    }

}