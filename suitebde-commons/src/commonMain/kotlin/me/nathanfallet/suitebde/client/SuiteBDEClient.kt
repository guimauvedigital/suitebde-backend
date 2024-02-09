package me.nathanfallet.suitebde.client

import me.nathanfallet.ktorx.models.api.AbstractAPIClient
import me.nathanfallet.ktorx.repositories.auth.AuthAPIRemoteRepository
import me.nathanfallet.ktorx.usecases.api.IGetTokenUseCase
import me.nathanfallet.suitebde.models.application.SuiteBDEEnvironment
import me.nathanfallet.suitebde.models.application.SuiteBDEJson
import me.nathanfallet.suitebde.repositories.associations.AssociationsRemoteRepository
import me.nathanfallet.suitebde.repositories.associations.DomainsInAssociationsRemoteRepository
import me.nathanfallet.suitebde.repositories.associations.SubscriptionsInAssociationsRemoteRepository
import me.nathanfallet.suitebde.repositories.clubs.ClubsRemoteRepository
import me.nathanfallet.suitebde.repositories.clubs.UsersInClubsRemoteRepository
import me.nathanfallet.suitebde.repositories.events.EventsRemoteRepository
import me.nathanfallet.suitebde.repositories.roles.PermissionsInRolesRemoteRepository
import me.nathanfallet.suitebde.repositories.roles.RolesRemoteRepository
import me.nathanfallet.suitebde.repositories.roles.UsersInRolesRemoteRepository
import me.nathanfallet.suitebde.repositories.users.SubscriptionsInUsersRemoteRepository
import me.nathanfallet.suitebde.repositories.users.UsersRemoteRepository
import me.nathanfallet.suitebde.repositories.web.WebMenusRemoteRepository
import me.nathanfallet.suitebde.repositories.web.WebPagesRemoteRepository

class SuiteBDEClient(
    getTokenUseCase: IGetTokenUseCase? = null,
    environment: SuiteBDEEnvironment = SuiteBDEEnvironment.PRODUCTION,
) : AbstractAPIClient(
    environment.baseUrl,
    getTokenUseCase,
    SuiteBDEJson.json
), ISuiteBDEClient {

    override val auth = AuthAPIRemoteRepository(this, prefix = "/api/v1")
    override val associations = AssociationsRemoteRepository(this)
    override val domainsInAssociations = DomainsInAssociationsRemoteRepository(this, associations)
    override val subscriptionsInAssociations = SubscriptionsInAssociationsRemoteRepository(this, associations)
    override val users = UsersRemoteRepository(this, associations)
    override val subscriptionsInUsers = SubscriptionsInUsersRemoteRepository(this, users)
    override val webMenus = WebMenusRemoteRepository(this, associations)
    override val webPages = WebPagesRemoteRepository(this, associations)
    override val events = EventsRemoteRepository(this, associations)
    override val roles = RolesRemoteRepository(this, associations)
    override val usersInRoles = UsersInRolesRemoteRepository(this, roles)
    override val permissionsInRoles = PermissionsInRolesRemoteRepository(this, roles)
    override val clubs = ClubsRemoteRepository(this, associations)
    override val usersInClubs = UsersInClubsRemoteRepository(this, clubs)

}
