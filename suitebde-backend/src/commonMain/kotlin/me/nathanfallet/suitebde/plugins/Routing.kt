package me.nathanfallet.suitebde.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.servers.Server
import me.nathanfallet.ktorx.extensions.info
import me.nathanfallet.ktorx.routers.openapi.OpenAPIRouter
import me.nathanfallet.suitebde.controllers.associations.AssociationsRouter
import me.nathanfallet.suitebde.controllers.associations.DomainsInAssociationsRouter
import me.nathanfallet.suitebde.controllers.auth.AuthRouter
import me.nathanfallet.suitebde.controllers.events.EventsRouter
import me.nathanfallet.suitebde.controllers.roles.RolesRouter
import me.nathanfallet.suitebde.controllers.users.UsersRouter
import me.nathanfallet.suitebde.controllers.web.WebMenusRouter
import me.nathanfallet.suitebde.controllers.web.WebPagesRouter
import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(IgnoreTrailingSlash)
    routing {
        val openAPI = OpenAPI().info {
            this.title = "Suite BDE API"
            this.description = "Suite BDE API"
            this.version = "1.0.0"
        }
        openAPI.servers(
            listOf(
                Server().description("Production server").url(SuiteBDEEnvironment.PRODUCTION.baseUrl),
                Server().description("Staging server").url(SuiteBDEEnvironment.DEVELOPMENT.baseUrl)
            )
        )

        val associationsRouter by inject<AssociationsRouter>()
        val domainsInAssociationsRouter by inject<DomainsInAssociationsRouter>()
        val authRouter by inject<AuthRouter>()
        val usersRouter by inject<UsersRouter>()
        val rolesRouter by inject<RolesRouter>()
        val webPagesRouter by inject<WebPagesRouter>()
        val webMenusRouter by inject<WebMenusRouter>()
        val eventsRouter by inject<EventsRouter>()

        val openAPIRouter = OpenAPIRouter()

        authenticate("api-v1-jwt", optional = true) {
            associationsRouter.createRoutes(this, openAPI)
            domainsInAssociationsRouter.createRoutes(this, openAPI)
            authRouter.createRoutes(this, openAPI)
            usersRouter.createRoutes(this, openAPI)
            rolesRouter.createRoutes(this, openAPI)
            webPagesRouter.createRoutes(this, openAPI)
            webMenusRouter.createRoutes(this, openAPI)
            eventsRouter.createRoutes(this, openAPI)

            openAPIRouter.createRoutes(this, openAPI)
        }

        staticResources("", "static")
    }
}
