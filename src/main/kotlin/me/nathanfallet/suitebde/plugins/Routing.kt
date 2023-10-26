package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import me.nathanfallet.suitebde.controllers.associations.AssociationRouter
import me.nathanfallet.suitebde.controllers.auth.AuthRouter
import me.nathanfallet.suitebde.controllers.users.UserRouter
import me.nathanfallet.suitebde.controllers.web.WebRouter
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        val webRouter by inject<WebRouter>()
        val associationRouter by inject<AssociationRouter>()
        val authRouter by inject<AuthRouter>()
        val userRouter by inject<UserRouter>()

        webRouter.createRoutes(this)
        associationRouter.createRoutes(this)
        authRouter.createRoutes(this)
        userRouter.createRoutes(this)

        staticResources("", "static")
    }
}
