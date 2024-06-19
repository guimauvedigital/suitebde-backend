package com.suitebde.client

import com.suitebde.models.application.SuiteBDEEnvironment
import com.suitebde.repositories.associations.AssociationsRemoteRepository
import com.suitebde.repositories.associations.DomainsInAssociationsRemoteRepository
import com.suitebde.repositories.associations.SubscriptionsInAssociationsRemoteRepository
import com.suitebde.repositories.auth.AuthAPIRemoteRepository
import com.suitebde.repositories.clubs.ClubsRemoteRepository
import com.suitebde.repositories.clubs.UsersInClubsRemoteRepository
import com.suitebde.repositories.events.EventsRemoteRepository
import com.suitebde.repositories.notifications.NotificationTokensRemoteRepository
import com.suitebde.repositories.notifications.NotificationsRemoteRepository
import com.suitebde.repositories.roles.PermissionsInRolesRemoteRepository
import com.suitebde.repositories.roles.RolesRemoteRepository
import com.suitebde.repositories.roles.UsersInRolesRemoteRepository
import com.suitebde.repositories.scans.ScansRemoteRepository
import com.suitebde.repositories.users.SubscriptionsInUsersRemoteRepository
import com.suitebde.repositories.users.UsersRemoteRepository
import com.suitebde.repositories.web.WebMenusRemoteRepository
import com.suitebde.repositories.web.WebPagesRemoteRepository
import dev.kaccelero.client.AbstractAPIClient
import dev.kaccelero.commons.auth.IGetTokenUseCase
import dev.kaccelero.commons.auth.ILogoutUseCase
import dev.kaccelero.commons.auth.IRenewTokenUseCase

class SuiteBDEClient(
    getTokenUseCase: IGetTokenUseCase,
    renewTokenUseCase: IRenewTokenUseCase,
    logoutUseCase: ILogoutUseCase,
    environment: SuiteBDEEnvironment = SuiteBDEEnvironment.PRODUCTION,
) : AbstractAPIClient(
    environment.baseUrl,
    getTokenUseCase,
    renewTokenUseCase,
    logoutUseCase
), ISuiteBDEClient {

    override val auth = AuthAPIRemoteRepository(this)
    override val associations = AssociationsRemoteRepository(this)
    override val domainsInAssociations = DomainsInAssociationsRemoteRepository(this, associations)
    override val subscriptionsInAssociations = SubscriptionsInAssociationsRemoteRepository(this, associations)
    override val notifications = NotificationsRemoteRepository(this)
    override val users = UsersRemoteRepository(this, associations)
    override val subscriptionsInUsers = SubscriptionsInUsersRemoteRepository(this, users)
    override val notificationTokens = NotificationTokensRemoteRepository(this, users)
    override val scans = ScansRemoteRepository(this, associations)
    override val webMenus = WebMenusRemoteRepository(this, associations)
    override val webPages = WebPagesRemoteRepository(this, associations)
    override val events = EventsRemoteRepository(this, associations)
    override val roles = RolesRemoteRepository(this, associations)
    override val usersInRoles = UsersInRolesRemoteRepository(this, roles)
    override val permissionsInRoles = PermissionsInRolesRemoteRepository(this, roles)
    override val clubs = ClubsRemoteRepository(this, associations)
    override val usersInClubs = UsersInClubsRemoteRepository(this, clubs)

}
