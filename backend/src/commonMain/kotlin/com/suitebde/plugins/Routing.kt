package com.suitebde.plugins

import com.suitebde.controllers.associations.AssociationsRouter
import com.suitebde.controllers.associations.DomainsInAssociationsRouter
import com.suitebde.controllers.associations.SubscriptionsInAssociationsRouter
import com.suitebde.controllers.auth.AuthRouter
import com.suitebde.controllers.clubs.ClubsRouter
import com.suitebde.controllers.clubs.UsersInClubsRouter
import com.suitebde.controllers.dashboard.DashboardRouter
import com.suitebde.controllers.events.EventsRouter
import com.suitebde.controllers.files.FilesRouter
import com.suitebde.controllers.notifications.NotificationTokensRouter
import com.suitebde.controllers.notifications.NotificationsRouter
import com.suitebde.controllers.roles.PermissionsInRolesRouter
import com.suitebde.controllers.roles.RolesRouter
import com.suitebde.controllers.roles.UsersInRolesRouter
import com.suitebde.controllers.root.RootRouter
import com.suitebde.controllers.scans.ScansRouter
import com.suitebde.controllers.users.SubscriptionsInUsersRouter
import com.suitebde.controllers.users.UsersRouter
import com.suitebde.controllers.web.WebMenusRouter
import com.suitebde.controllers.web.WebPagesRouter
import com.suitebde.controllers.webhooks.WebhooksRouter
import com.suitebde.models.application.SuiteBDEEnvironment
import dev.kaccelero.routers.OpenAPIRouter
import dev.kaccelero.routers.createRoutes
import dev.kaccelero.routers.info
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.servers.Server
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
                get<FilesRouter>(),
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
