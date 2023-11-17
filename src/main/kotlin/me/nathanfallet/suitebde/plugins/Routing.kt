package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.controllers.associations.DomainsInAssociationsRouter
import me.nathanfallet.suitebde.controllers.auth.AuthRouter
import me.nathanfallet.suitebde.controllers.users.UsersRouter
import me.nathanfallet.suitebde.controllers.web.WebPagesRouter
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        val associationsRouter by inject<AssociationsRouter>()
        val domainsInAssociationsRouter by inject<DomainsInAssociationsRouter>()
        val authRouter by inject<AuthRouter>()
        val usersRouter by inject<UsersRouter>()
        val webPagesRouter by inject<WebPagesRouter>()

        authenticate("api-v1-jwt", optional = true) {
            associationsRouter.createRoutes(this)
            domainsInAssociationsRouter.createRoutes(this)
            authRouter.createRoutes(this)
            usersRouter.createRoutes(this)
            webPagesRouter.createRoutes(this)
        }

        staticResources("", "static")
    }
}
