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
import me.nathanfallet.suitebde.controllers.associations.SubscriptionsInAssociationsRouter
import me.nathanfallet.suitebde.controllers.auth.AuthRouter
import me.nathanfallet.suitebde.controllers.clubs.ClubsRouter
import me.nathanfallet.suitebde.controllers.clubs.UsersInClubsRouter
import me.nathanfallet.suitebde.controllers.dashboard.DashboardRouter
import me.nathanfallet.suitebde.controllers.events.EventsRouter
import me.nathanfallet.suitebde.controllers.notifications.NotificationTokensRouter
import me.nathanfallet.suitebde.controllers.notifications.NotificationsRouter
import me.nathanfallet.suitebde.controllers.roles.PermissionsInRolesRouter
import me.nathanfallet.suitebde.controllers.roles.RolesRouter
import me.nathanfallet.suitebde.controllers.roles.UsersInRolesRouter
import me.nathanfallet.suitebde.controllers.root.RootRouter
import me.nathanfallet.suitebde.controllers.scans.ScansRouter
import me.nathanfallet.suitebde.controllers.users.SubscriptionsInUsersRouter
import me.nathanfallet.suitebde.controllers.users.UsersRouter
import me.nathanfallet.suitebde.controllers.web.WebMenusRouter
import me.nathanfallet.suitebde.controllers.web.WebPagesRouter
import me.nathanfallet.suitebde.controllers.webhooks.WebhooksRouter
import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment
import org.koin.ktor.ext.get

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

        authenticate("api-v1-jwt", optional = true) {
            listOf(
                get<RootRouter>(),
                get<WebhooksRouter>(),
                get<DashboardRouter>(),
                get<NotificationsRouter>(),
                get<AssociationsRouter>(),
                get<DomainsInAssociationsRouter>(),
                get<SubscriptionsInAssociationsRouter>(),
                get<AuthRouter>(),
                get<UsersRouter>(),
                get<SubscriptionsInUsersRouter>(),
                get<NotificationTokensRouter>(),
                get<ScansRouter>(),
                get<RolesRouter>(),
                get<UsersInRolesRouter>(),
                get<PermissionsInRolesRouter>(),
                get<WebPagesRouter>(),
                get<WebMenusRouter>(),
                get<EventsRouter>(),
                get<ClubsRouter>(),
                get<UsersInClubsRouter>(),
                OpenAPIRouter(), // OpenAPI should be last
            ).forEach {
                it.createRoutes(this, openAPI)
            }
        }

        staticResources("", "static")
    }
}
