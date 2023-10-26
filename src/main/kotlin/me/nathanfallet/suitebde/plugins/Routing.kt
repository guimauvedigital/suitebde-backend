package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import me.nathanfallet.suitebde.controllers.associations.AssociationRouter
import me.nathanfallet.suitebde.controllers.auth.AuthRouter
import me.nathanfallet.suitebde.controllers.users.UserRouter
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        val associationRouter by inject<AssociationRouter>()
        val authRouter by inject<AuthRouter>()
        val userRouter by inject<UserRouter>()

        associationRouter.createRoutes(this)
        authRouter.createRoutes(this)
        userRouter.createRoutes(this)
    }
}
