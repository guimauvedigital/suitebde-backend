package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import me.nathanfallet.suitebde.controllers.associations.AssociationController
import me.nathanfallet.suitebde.controllers.users.UserController
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    routing {
        val associationController by inject<AssociationController>()
        val userController by inject<UserController>()

        associationController.createRoutes(this)
        userController.createRoutes(this)
    }
}
